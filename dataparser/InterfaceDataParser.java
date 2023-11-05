package com.elevator.unit.Andulir.dataparser;


import com.elevator.unit.Andulir.annotation.ATest;
import com.elevator.unit.Andulir.datagenerator.InterfaceDataGeneration;
import com.elevator.unit.Andulir.dataparser.entity.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.*;
import java.util.*;

//接口数据分析器
//这个分析器的作用是分析接口基本信息和参数的结构,并且以xml的形式存储起来
public class InterfaceDataParser {

    //这个方法的作用是判断方法的参数是否是自定义的请求类
    public static boolean isBasicTypeOrList(Parameter parameter) {
        List<Class<?>> basicTypes = Arrays.asList(
                int.class, long.class, short.class, byte.class, char.class, double.class, float.class, boolean.class,
                Integer.class, Long.class, Short.class, Byte.class, Character.class, Double.class, Float.class, Boolean.class,
                String.class, List.class
        );
        Class<?> parameterType = parameter.getType();
        return basicTypes.contains(parameterType);
    }

    public  List<String> getMemberVariableTypes(String className) throws ClassNotFoundException {
        List<String> memberVariableTypes = new ArrayList<>();
        // 使用反射加载类
        Class<?> clazz = Class.forName(className);
        // 获取类的所有成员变量
        Field[] fields = clazz.getDeclaredFields();
        // 遍历成员变量并将类型转换为字符串格式
        for (Field field : fields) {
            String fieldType;
            if (field.getType() == List.class) {
                // 处理List类型
                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length > 0) {
                        fieldType = "List<" + actualTypeArguments[0].getTypeName() + ">";
                    } else {
                        fieldType = "List";
                    }
                } else {
                    fieldType = "List";
                }
            } else {
                fieldType = field.getType().getName();
            }
            memberVariableTypes.add(fieldType);
        }
        return memberVariableTypes;
    }

    //这个方法的作用是获取方法的参数类型列表
    public List<String> getMethodParameterTypes(Method method) throws ClassNotFoundException {
        List<String> parameterTypes = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            //返回false.则说明该接口为请求类，进行请求类的特殊处理逻辑
            if(!isBasicTypeOrList(parameter)){
                String name = parameter.getType().getName();
                List<String> result = getMemberVariableTypes(name);
                result.add("请求类");
                result.add(name);
                return result;
            }
            String paramType = parameter.getType().getName();
            //判断是否为list类型
            if (List.class.isAssignableFrom(parameter.getType())) {
                Type genericType = parameter.getParameterizedType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length > 0) {
                        Class<?> elementType = (Class<?>) actualTypeArguments[0];
                        paramType = "List<" + elementType.getName() + ">";
                    }
                }
            }
            parameterTypes.add(paramType);
        }
        return parameterTypes;
    }


    //这个方法的作用是判断xml文件是否已经生成,如果未生成,就生成一个全新的xml文件
   public void interfaceFileInitialization() throws JAXBException {
           // 检查项目下是否存在名为"atest.xml"的文件
           File xmlFile = new File("atest.xml");
           if (xmlFile.exists()) {
               System.out.println("文件已经存在");
           } else {
               // 创建JAXB上下文
               JAXBContext context = JAXBContext.newInstance(ATestEntity.class);

               // 创建Marshaller
               Marshaller marshaller = context.createMarshaller();

               // 创建ATestEntity对象并填充数据
               ATestEntity data = new ATestEntity();

               // 将数据序列化为XML并保存到文件
               marshaller.marshal(data, new File("atest.xml"));
               System.out.println("XML文件已生成。");
           }
   }

    //这个方法的作用是根据传入的参数生成对应的xml文件
    public String generateXML(String controllerName, String methodMappingName,Integer exampleNumber,List<String> parameterTypes) throws JAXBException {

      try {

        //首先处理parameter
          String parameter = "无";
          if(parameterTypes.size()>2){
              if(parameterTypes.get(parameterTypes.size()-2).equals("请求类")){
                parameter = parameterTypes.get(parameterTypes.size()-1);
              }
          }
        List<ParameterMapping> parameterMappings = new ArrayList<>();
        for (String parameterType : parameterTypes) {
            if(parameterType.equals("请求类")){
                continue;
            }
            if(parameterType.equals(parameter)){
                continue;
            }

            ParameterMapping parameterMapping = new ParameterMapping();
            parameterMapping.setType(parameterType);
            if(!InterfaceDataGeneration.generateRandomData(parameterType).equals("list")){
                parameterMapping.setValue(InterfaceDataGeneration.generateRandomData(parameterType));
            }else {
                parameterMapping.setValue(InterfaceDataGeneration.generateRandomDataList(InterfaceDataGeneration.extractGenericType(parameterType)));
            }
            parameterMappings.add(parameterMapping);
        }

        //然后处理Example
        List<ExampleMapping> exampleMappings = new ArrayList<>();
        for (int i = 0; i < exampleNumber; i++) {
            ExampleMapping exampleMapping = new ExampleMapping();
            exampleMapping.setParameterMappings(parameterMappings);
            exampleMappings.add(exampleMapping);
        }

        //因为可能会存在method和controller已经存在的情况,所以现在必须正向处理

        //处理ATestEntity,这个对象按照正常流程不会不存在
        ATestEntity aTestEntity = null;
        try {
            JAXBContext context = JAXBContext.newInstance(ATestEntity.class);

            // Deserialize existing XML content
            Unmarshaller unmarshaller = context.createUnmarshaller();
            aTestEntity = (ATestEntity) unmarshaller.unmarshal(new File("atest.xml"));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        List<ControllerMapping> controllerMappings =null;
        //处理Controller
        if (CollectionUtils.isEmpty(aTestEntity.getControllers())) {
            controllerMappings = new ArrayList<>();
        }else {
            controllerMappings = aTestEntity.getControllers();
        }


        //这段逻辑判断controller是否存在,如果存在则直接操作原有的controller,如果不存在则新建一个
          ControllerMapping controllerMapping1 = null;
          Iterator<ControllerMapping> iterator = controllerMappings.iterator();
          while (iterator.hasNext()) {
              ControllerMapping controllerMapping = iterator.next();
              if (controllerMapping.getName().equals(controllerName)) {
                  controllerMapping1 = controllerMapping;
                  iterator.remove(); // 使用迭代器的 remove 方法来安全地删除元素
              }
          }

        if (controllerMapping1 == null) {
            controllerMapping1 = new ControllerMapping();
            controllerMapping1.setName(controllerName);
            List<MethodMapping> methodMappings = new ArrayList<>();
            controllerMapping1.setMethodMappings(methodMappings);
        }

        //处理Method
        //如果method已经存在的话,说明已经自动根据这个注解生成过一次了,就不需要再继续处理了,直接退出.

        List<MethodMapping> methodMappings = controllerMapping1.getMethodMappings();
        MethodMapping methodMapping = null;

        //只有在方法列表不为0的情况下,才需要判断方法是否存在,否则直接新建.
        if(methodMappings.size()!=0){
            for (MethodMapping methodMapping1 : methodMappings) {
                if (methodMapping1.getName().equals(methodMappingName)) {
                    return "已经存在方法";
                }
            }
            methodMapping = new MethodMapping();
        }else {
            methodMapping = new MethodMapping();
        }

        methodMapping.setName(methodMappingName);
        //0为这个方法测试未关闭,自动化测试会测试这个方法,1为已经关闭,测试时会忽略.
        methodMapping.setStatus(0);
        //判断请求类,如果是，则isRequest字段就存储请求类的名称,如果不是，则存储不是请求类。
        if(parameterTypes.size()>2){
            if(parameterTypes.get(parameterTypes.size()-2).equals("请求类")){
                methodMapping.setIsRequest(parameterTypes.get(parameterTypes.size()-1));
            }else{
                methodMapping.setIsRequest("不是请求类");
            }
        }else{
            methodMapping.setIsRequest("不是请求类");
        }
        methodMapping.setExamples(exampleMappings);

        //保存
        methodMappings.add(methodMapping);
        controllerMapping1.setMethodMappings(methodMappings);
        controllerMappings.add(controllerMapping1);
        aTestEntity.setControllers(controllerMappings);

        //写入
        JAXBContext context = JAXBContext.newInstance(ATestEntity.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 启用格式化选项
        marshaller.marshal(aTestEntity, new File("atest.xml"));
        return "成功";
    }catch (NullPointerException e){
      e.printStackTrace();
    }
      return "成功";
}

    //这个方法的作用是扫描带有注解的方法,并获取其参数列表
    public void conversionInterfaceInformation(String basePackage){
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));

        Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents(basePackage);
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(ATest.class)) {
                        ATest annotation = method.getAnnotation(ATest.class);
                        // 处理加了注解的方法
                        System.out.println("搜索到带此注解的方法,方法名字为:" + method.getName()+"接口相关数据已经生成.");
                        generateXML(clazz.getName(),method.getName(),annotation.value(),getMethodParameterTypes(method));
                    }
                }
            } catch (ClassNotFoundException | NullPointerException e) {
                e.printStackTrace();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

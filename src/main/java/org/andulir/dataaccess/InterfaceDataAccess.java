package org.andulir.dataaccess;

import org.andulir.dataparser.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Service
public class InterfaceDataAccess {



    @Autowired
    private ApplicationContext applicationContext;

    public void run() throws InvocationTargetException, IllegalAccessException {
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
        System.out.println(applicationContext.getBean("inspectionItemController"));
        Object bean = applicationContext.getBean("inspectionItemController");
        //获取方法
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.getName().equals("findInspectionResult")) {
                System.out.println("已经成功获取到方法"+method.getName());
                System.out.println(method.invoke(bean));
            }
        }
    }

    public static String getSimpleClassName(String fullClassName) {
        int lastDotIndex = fullClassName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return Character.toLowerCase(fullClassName.charAt(lastDotIndex + 1)) + fullClassName.substring(lastDotIndex + 2);
        } else {
            return Character.toLowerCase(fullClassName.charAt(0)) + fullClassName.substring(1);
        }
    }

    public static Object[] convertListToObjects(List<ParameterMapping> parameterList) {
        Object[] objects = new Object[parameterList.size()];

        for (int i = 0; i < parameterList.size(); i++) {
            ParameterMapping parameter = parameterList.get(i);
            Object value = convertValue(parameter.getType(), parameter.getValue());
            objects[i] = value;
        }

        return objects;
    }

    private static Object convertValue(String type, String value) {
        try {
            if ("int".equals(type) || "java.lang.Integer".equals(type)) {
                return Integer.parseInt(value);
            } else if ("long".equals(type) || "java.lang.Long".equals(type)) {
                return Long.parseLong(value);
            } else if ("short".equals(type) || "java.lang.Short".equals(type)) {
                return Short.parseShort(value);
            } else if ("byte".equals(type) || "java.lang.Byte".equals(type)) {
                return Byte.parseByte(value);
            } else if ("char".equals(type) || "java.lang.Character".equals(type)) {
                return value.charAt(0);
            } else if ("double".equals(type) || "java.lang.Double".equals(type)) {
                return Double.parseDouble(value);
            } else if ("float".equals(type) || "java.lang.Float".equals(type)) {
                return Float.parseFloat(value);
            } else if ("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
                return Boolean.parseBoolean(value);
            } else if ("java.lang.String".equals(type)) {
                return value;
            } else {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid value for type " + type + ": " + value);
        }
    }

    public  void test() throws InvocationTargetException, IllegalAccessException {
        ATestEntity aTestEntity = null;
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(ATestEntity.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        // Deserialize existing XML content
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        try {
            aTestEntity = (ATestEntity) unmarshaller.unmarshal(new File("atest.xml"));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        List<ControllerMapping> controllerMappings = aTestEntity.getControllers();
        for(ControllerMapping controllerMapping:controllerMappings){
            List<MethodMapping> methodMappings = controllerMapping.getMethodMappings();
            for (MethodMapping methodMapping:methodMappings){
                if(methodMapping.getStatus()==1){
                    continue;
                }else {
                  List<ExampleMapping> exampleMappings = methodMapping.getExamples();
                  for(ExampleMapping exampleMapping:exampleMappings){
                      List<ParameterMapping> parameterMappings = exampleMapping.getParameterMappings();
                      Object[] args = convertListToObjects(parameterMappings);
                      //获取bean
                      Object bean = applicationContext.getBean(getSimpleClassName(controllerMapping.getName()));
                      //运行方法
                      for (Method method : bean.getClass().getDeclaredMethods()) {
                          if (method.getName().equals(methodMapping.getName())) {
                              System.out.println("开始测试方法,方法名为"+method.getName());
                              System.out.println("其具体值为:");
                              for(ParameterMapping parameterMapping:parameterMappings){
                                  System.out.println("类型"+parameterMapping.getType());
                                  System.out.println("值"+parameterMapping.getValue());
                              }
                              System.out.println("测试结果为"+method.invoke(bean,args));
                              break;
                          }
                      }
                  }
                }
            }
        }
        System.out.println("所有接口测试完毕");
    }
}

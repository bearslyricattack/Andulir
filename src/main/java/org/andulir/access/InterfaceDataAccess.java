package org.andulir.access;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.andulir.exception.AndulirSystemException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
public class InterfaceDataAccess {
    @Autowired
    private Document document;
    @Autowired
    private BasicDataAccess basicDataAccess;
    @Autowired
    private ListDataAccess listDataAccess;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private ApplicationContext applicationContext;

    public void testMethod() {
        //得到xml根节点
        Element rootElement = document.getRootElement();
        List<Element> controllerMappings = rootElement.elements("controllerMapping");
        //遍历controllerMapping节点
        for (Element controllerMapping : controllerMappings) {
            String controllerName = controllerMapping.attribute("name").getText();
            String[] split = controllerName.split("\\.");

            String simpleControllerName = split[split.length - 1];
            String controllerName1 = Character.toLowerCase(simpleControllerName.charAt(0)) + simpleControllerName.substring(1);
            Object controllerInstance = applicationContext.getBean(controllerName1);
            Class<?> controller = controllerInstance.getClass();

//            Object controllerInstance = null;
//            try {
//                //通过controller的name反射得到类对象
//                controller = Class.forName(controllerName);
//                controllerInstance = controller.newInstance();
//            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//                throw new AndulirSystemException(controllerName + "类实例生成异常！" + e.getMessage());
//            }

            List<Element> methodMappings = controllerMapping.elements();
            List<CompletableFuture> futures = new ArrayList<>();
            //遍历methodMapping节点
            for (Element methodMapping : methodMappings) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    //得到方法名
                    String methodName = methodMapping.attribute("name").getText();
                    Method invokedMethod = null;
                    for (Method method : controller.getMethods()) {
                        if (method.getName().equals(methodName)) {
                            invokedMethod = method;
                            break;
                        }
                    }
                    //得到status
                    String status = methodMapping.attribute("status").getText();
                    if ("0".equals(status)) {
                        return;
                    }


                    Element parameterMapping = methodMapping.element("parameterMapping");
                    List<Element> typeMappings = parameterMapping.elements();
                    Object[] args = new Object[typeMappings.size()];
                    for (int i = 0; i < Integer.parseInt(status); i++) {
                        int count = 0;
                        for (Element typeMapping : typeMappings) {
                            String typeName = typeMapping.element("name").getText();
                            List<Element> values = typeMapping.elements("value");
                            if (values.isEmpty()) {
                                return;
                            }
                            if (values.size() != Integer.parseInt(status)) {
                                //自己改测试用例后可能会出现测试用例个数和status值不相同，上面按照status
                                log.warn("{}中{}方法测试用例个数和status值不相同！", controllerName, methodName);
                            }

                            Object arg = null;
                            String value = values.get(i).getText();
                            try {
                                if (typeName.contains("java.util.List")) { //如果是List类型
                                    Element genericsTypeMapping = typeMapping.element("genericsMapping").element("typeMapping");
                                    if (genericsTypeMapping == null) {
                                        log.error("{}类中的{}方法的List参数没有指定泛型！", controllerName, methodName);
                                        return;
                                        //没有泛型怎么处理？此处直接退出测试方法
                                    } else {
                                        arg = listDataAccess.convertRandomData2Obj(typeName, value);
                                    }

                                } else { //如果是请求类或基本数据类型
                                    arg = basicDataAccess.convertRandomData2Obj(typeName, value);
                                }
                                args[count++] = arg;
                            } catch (ClassNotFoundException e) {
                                throw new AndulirSystemException(controllerName + "类中" + methodName + "方法测试出现异常！" + e.getMessage());
                            } catch (JsonProcessingException e) {
                                throw new AndulirSystemException(controllerName + "类中" + methodName + "方法的随机测试用例反序列化过程出现异常！" + e.getMessage());
                            }
                            //利用反射来测试
                            if (invokedMethod == null) {
                                log.error("未找到方法{}！", methodName);
                                return;
                            }
                            try {
                                Object result = invokedMethod.invoke(controllerInstance, args);
                                log.info("{}类中的{}方法第{}次测试完毕，测试结果为{}", controllerName, methodName, i + 1, result);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new AndulirSystemException(controllerName + "类中" + methodName + "方法的反射调用出现异常！" + e.getMessage());
                            }
                        }
                    }

                    log.info("{}类中的{}方法已测试完毕！", controllerName, methodName);
                }, threadPoolExecutor);
                futures.add(future);
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
            log.info("{}类中的方法全部测试完毕！",controllerName);

        }
    }
}

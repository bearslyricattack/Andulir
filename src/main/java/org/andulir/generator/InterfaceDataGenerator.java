package org.andulir.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.andulir.exception.AndulirSystemException;
import org.andulir.utils.XMLUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static org.andulir.utils.TypeUtils.isBasicType;

@Component
@Slf4j
public class InterfaceDataGenerator {
    @Autowired
    private DataGenerator basicDataGenerator;
    @Autowired
    private DataGenerator listDataGenerator;
    @Autowired
    private DataGenerator requestDataGenerator;
    @Autowired
    private Document document;
    @Autowired
    private File file;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    public void generateRandomData() {
        Element rootElement = document.getRootElement();
        List<Element> controllerMappings = rootElement.elements("controllerMapping");
        for (Element controllerMapping : controllerMappings) {
            String controllerName = controllerMapping.attribute("name").getText();
            List<Element> methodMappings = controllerMapping.elements();
            for (Element methodMapping : methodMappings) {
                String name = methodMapping.attribute("name").getText();
                String status = methodMapping.attribute("status").getText();
                if ("0".equals(status)) {
                    return;
                }
                Element parameterMapping = methodMapping.element("parameterMapping");
                List<Element> typeMappings = parameterMapping.elements();

                List<CompletableFuture> futures = new ArrayList<>();
                for (Element typeMapping : typeMappings) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        String value = null;
                        String typeName = typeMapping.element("name").getText();

                        for (int i = 0; i < Integer.parseInt(status); i++) {
                            try {
                                if (typeName.contains("java.util.List")) {
                                    if (typeName.equals("java.util.List")) {
                                        log.error("{}类中的{}方法的List参数没有指定泛型！", controllerName, name);
                                        return;
                                    }
                                    value = listDataGenerator.generateRandomData(typeName, typeMapping);
                                } else if (isBasicType(typeName)) {
                                    value = basicDataGenerator.generateRandomData(typeName, typeMapping);
                                } else {
                                    value = requestDataGenerator.generateRandomData(typeName, typeMapping);
                                }
                            } catch (ClassNotFoundException e) {
                                throw new AndulirSystemException(controllerName + "类中" + name + "方法生成随机测试用例出现异常！" + e.getMessage());
                            } catch (JsonProcessingException e) {
                                throw new AndulirSystemException(controllerName + "类中" + name + "方法的随机测试用例序列化过程出现异常！" + e.getMessage());
                            }


                            if (value != null) {
                                XMLUtils.addBasicValue(typeMapping, value);
                                XMLUtils.writeXML(document, file);
                            }
                        }
                    }, threadPoolExecutor);
                    futures.add(future);
                }
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
                log.info("ATest注解方法[{}]接口参数随机用例已生成。",name);
            }
        }
    }

}

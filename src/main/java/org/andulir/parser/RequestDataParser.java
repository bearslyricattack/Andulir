package org.andulir.parser;

import org.andulir.utils.TypeUtils;
import org.andulir.utils.XMLUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
@Component
public class RequestDataParser implements DataParser {
    @Autowired
    private DataParser listDataParser;
    @Autowired
    private DataParser basicDataParser;
    @Override
    public void parseData(Type type, Element element) {
        String className = type.getTypeName();
        Element typeMapping = XMLUtils.addTypeMapping(element, className);

        // 使用反射加载类
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // 获取类的所有成员变量
        Field[] fields = clazz.getDeclaredFields();
        Element attributeMapping;
        if (fields.length == 0) {
            return;
        } else {
            attributeMapping = XMLUtils.addAttributeMapping(typeMapping);
        }
        for (Field field : fields) {
            if (List.class.isAssignableFrom(field.getType())) {
                listDataParser.parseData(field.getGenericType(),attributeMapping);
            } else if (TypeUtils.isBasicType(field.getType())) {
                basicDataParser.parseData(field.getType(),attributeMapping);
            } else {
                parseData(field.getType(),attributeMapping);
            }
        }
    }
}

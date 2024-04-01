package org.andulir.parser;

import org.andulir.utils.TypeUtils;
import org.andulir.utils.XMLUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
@Component
public class ListDataParser implements DataParser {
    @Autowired
    @Lazy
    private DataParser requestDataParser;
    @Override
    public void parseData(Type type, Element element) {
        Element typeMapping = XMLUtils.addTypeMapping(element, type.getTypeName());
        parse(type,typeMapping);
    }
    public void parse(Type type, Element typeMapping) {
        Type genericType = type;
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                Type elementType =  actualTypeArguments[0];
                if (TypeUtils.isBasicType(elementType)) {
                    XMLUtils.addGenericsMapping(typeMapping,elementType.getTypeName());
                } else if (elementType.getTypeName().contains("java.util.List")) {
                    Element typeMapping1 = XMLUtils.addGenericsMapping(typeMapping, elementType.getTypeName());
                    parse(elementType,typeMapping1);
                } else {
                    Element genericsMapping = XMLUtils.addGenericsMapping(typeMapping);
                    requestDataParser.parseData(elementType,genericsMapping);
                }
            }
        }
    }
}

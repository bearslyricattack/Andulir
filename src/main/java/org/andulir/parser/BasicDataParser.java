package org.andulir.parser;

import org.andulir.utils.XMLUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * 基本数据类型的解析器
 */
@Component
public class BasicDataParser implements DataParser {
    @Override
    public void parseData(Type type, Element element) {
        XMLUtils.addTypeMapping(element,type.getTypeName());
    }
}

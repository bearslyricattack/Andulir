package org.andulir.parser;

import org.dom4j.Element;

import java.lang.reflect.Type;

public interface DataParser {
    void parseData(Type type, Element element) throws ClassNotFoundException;
}

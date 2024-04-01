package org.andulir.generator;

import org.dom4j.Element;

public interface DataGenerator {

    String generateRandomData(String typeName, Element typeMapping);
}

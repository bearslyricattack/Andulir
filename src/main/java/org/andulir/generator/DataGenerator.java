package org.andulir.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dom4j.Element;

public interface DataGenerator {

    String generateRandomData(String typeName, Element typeMapping) throws ClassNotFoundException, JsonProcessingException;
}

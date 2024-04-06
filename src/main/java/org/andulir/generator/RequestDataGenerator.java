package org.andulir.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.jemos.podam.api.PodamFactory;

@Component
public class RequestDataGenerator implements DataGenerator {
    @Autowired
    private PodamFactory podamFactory;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public String generateRandomData(String typeName, Element typeMapping) throws ClassNotFoundException, JsonProcessingException {
        Class<?> clazz = null;
        clazz = Class.forName(typeName);
        Object value = podamFactory.manufacturePojo(clazz);
        return objectMapper.writeValueAsString(value);
    }
}

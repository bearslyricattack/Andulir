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
    public String generateRandomData(String typeName, Element typeMapping) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Object value = podamFactory.manufacturePojo(clazz);
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}

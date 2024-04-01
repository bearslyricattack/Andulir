package org.andulir.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.jemos.podam.api.PodamFactory;

@Component
public class ListDataGenerator implements DataGenerator {
    @Autowired
    private PodamFactory podamFactory;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public String generateRandomData(String typeName, Element typeMapping) {
        Class<?> listClass = null;
        Class<?> gClass = null;
        try {
            listClass = Class.forName("java.util.List");

            gClass = Class.forName(typeName.substring(typeName.indexOf('<') + 1, typeName.length() - 1));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Object value = podamFactory.manufacturePojo(listClass, gClass);
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}

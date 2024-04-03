package org.andulir.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.andulir.entity.ParameterizedTypeImpl;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.jemos.podam.api.PodamFactory;

import java.lang.reflect.Type;


@Component
public class ListDataGenerator implements DataGenerator {
    @Autowired
    private PodamFactory podamFactory;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String generateRandomData(String typeName, Element typeMapping) {
        Class<?> listClass = null;
        String className = null;
        Type genericsType = null;
        int count = 0;
        try {
            listClass = Class.forName("java.util.List");
            className = typeName.substring(typeName.indexOf('<') + 1, typeName.length() - 1);
            while (className.contains("java.util.List")) {
                count++;
                className = className.substring(className.indexOf('<') + 1, className.length() - 1);
            }

            genericsType = Class.forName(className);
            for (int i = 0; i < count; i++) {
                genericsType = new ParameterizedTypeImpl(new Type[]{genericsType}, null, listClass);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Object value = podamFactory.manufacturePojo(listClass, genericsType);
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}

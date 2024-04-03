package org.andulir.access;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BasicDataAccess implements DataAccess {
    @Autowired
    private ObjectMapper objectMapper;
    public Object convertRandomData2Obj(String typeName, String value) {
        Class<?> basicClass = null;
        try {
            basicClass = Class.forName(typeName);
            return objectMapper.readValue(value, basicClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

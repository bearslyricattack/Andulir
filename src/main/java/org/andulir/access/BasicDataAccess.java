package org.andulir.access;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BasicDataAccess implements DataAccess {
    @Autowired
    private ObjectMapper objectMapper;
    public Object convertRandomData2Obj(String typeName, String value) throws ClassNotFoundException, JsonProcessingException {
        Class<?> basicClass = Class.forName(typeName);
        return objectMapper.readValue(value, basicClass);
    }
}

package org.andulir.access;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.andulir.entity.ParameterizedTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class ListDataAccess implements DataAccess {
    @Autowired
    private ObjectMapper objectMapper;

    public Object convertRandomData2Obj(String typeName, String value) throws ClassNotFoundException, JsonProcessingException {
        Class<?> listClass = null;
        Type genericsListType = null;
        int count = 0;

        listClass = Class.forName("java.util.List");
        while (typeName.contains("java.util.List")) {
            count++;
            typeName = typeName.substring(typeName.indexOf('<') + 1, typeName.length() - 1);
        }

        genericsListType = Class.forName(typeName);
        for (int i = 0; i < count; i++) {
            genericsListType = new ParameterizedTypeImpl(new Type[]{genericsListType}, null, listClass);
        }

        Type finalGenericsListType = genericsListType;
        List<?> listValue = objectMapper.readValue(value, new TypeReference<>() {
            @Override
            public Type getType() {
                return finalGenericsListType;
            }
        });
        return listValue;

    }
}

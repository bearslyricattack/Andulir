package org.andulir.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        Class<?> gClass = null;
        String className=null;
        Type actualType=null;
        ParameterizedTypeImpl beforeType=null;
        int count=0;
        try {
            listClass = Class.forName("java.util.List");
            className=typeName.substring(typeName.indexOf('<') + 1, typeName.length() - 1);
            while(className.contains("java.util.List")){
                count++;
                className=className.substring(className.indexOf('<')+1,className.length()-1);
            }
            Type[] types=new Type[count+1];
            for (int i = 0; i < count; i++) {
                types[i]=listClass;
            }
            types[count]=Class.forName(className);
            for (int i = types.length - 1; i > 0; i--) {
                beforeType = new ParameterizedTypeImpl(new Type[]{beforeType == null ? types[i] : beforeType}, null, types[i - 1]);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Object value = podamFactory.manufacturePojo(listClass, beforeType);
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}

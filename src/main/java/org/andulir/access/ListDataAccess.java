package org.andulir.access;

import org.andulir.entity.ParameterizedTypeImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class ListDataAccess implements DataAccess {
    @Autowired
    private ObjectMapper objectMapper;

    public Object convertRandomData2Obj(String typeName, String value) {
        Class<?> listClass = null;
        Type genericsListType = null;
        int count = 0;
        try {
            listClass = Class.forName("java.util.List");
            //得到嵌套了多少层List
            while (typeName.contains("java.util.List")) {
                count++;
                typeName = typeName.substring(typeName.indexOf('<') + 1, typeName.length() - 1);
            }

            //最后剩下的typeName就是请求类或基本数据类型
            //最里面一层泛型就是最后剩下的typeName
            genericsListType = Class.forName(typeName);

            //一层一层套List，构建最终的嵌套List
            for (int i = 0; i < count; i++) {
                genericsListType = new ParameterizedTypeImpl(new Type[]{genericsListType}, null, listClass);
            }

            Type finalGenericsListType = genericsListType;
            //解析json
            List<?> listValue = objectMapper.readValue(value, new TypeReference<>() {
                @Override
                public Type getType() {
                    return finalGenericsListType;
                }
            });
            return listValue;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

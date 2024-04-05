package org.andulir.access;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DataAccess {
    Object convertRandomData2Obj(String typeName, String value) throws ClassNotFoundException, JsonProcessingException;
}

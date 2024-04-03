package org.andulir.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Data
@AllArgsConstructor
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Type ownerType;
    private final Type rawType;
}

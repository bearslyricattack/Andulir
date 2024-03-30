package org.andulir.utils;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 关于类型的工具类
 */
public class TypeUtils {
    public static boolean isBasicType(Parameter parameter) {
        Class<?> parameterType = parameter.getType();
        return isBasicType(parameterType);
    }

    public static boolean isBasicType(Type type) {
        Set basicTypes = new HashSet(Arrays.asList(
                int.class, long.class, short.class, byte.class, char.class, double.class, float.class, boolean.class,
                Integer.class, Long.class, Short.class, Byte.class, Character.class, Double.class, Float.class, Boolean.class,
                String.class, Date.class
        ));
        return basicTypes.contains(type);
    }

    public static boolean isBasicType(String typeName) {
        typeName = switchToPackageClass(typeName);
//        Set<String> basicType = new HashSet<>(Arrays.asList("int","long","short","byte","char","double","float","boolean"));
//        if (basicType.contains(typeName)) {
//            return true;
//        }
        Class<?> aClass = null;
        try {
            aClass = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return isBasicType(aClass);
    }

    // Class.forName不适用于基本数据类型
    public static String switchToPackageClass(String typeName) {
        switch (typeName) {
            case "int":
                typeName = "java.lang.Integer";
                break;
            case "long":
                typeName = "java.lang.Long";
                break;
            case "short":
                typeName = "java.lang.Short";
                break;
            case "byte":
                typeName = "java.lang.Byte";
                break;
            case "char":
                typeName = "java.lang.Character";
                break;
            case "double":
                typeName = "java.lang.Double";
                break;
            case "float":
                typeName = "java.lang.Float";
                break;
            case "boolean":
                typeName = "java.lang.Boolean";
                break;
        }
        return typeName;
    }
}

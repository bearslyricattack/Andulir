package com.elevator.unit.Andulir.datagenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jfinal.ext.kit.DateKit.dateFormat;

public class InterfaceDataGeneration {


    public static String extractGenericType(String input) {
        // 使用正则表达式来匹配<>中的内容
        Pattern pattern = Pattern.compile("<(.*?)>");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            // 提取匹配到的内容
            return matcher.group(1);
        } else {
            // 如果没有匹配到，则返回空字符串或者抛出异常，取决于你的需求
            return "";
        }
    }

    private static final Random random = new Random();
    public static String generateRandomData(String className) {
        if(className.length()>5){
            String firstFiveCharacters = className.substring(0, Math.min(5,className.length()));
            if(firstFiveCharacters.equals("List<")){
                return "list";
            }
        }
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz == int.class || clazz == Integer.class) {
                return Integer.toString(random.nextInt());
            } else if (clazz == long.class || clazz == Long.class) {
                return Long.toString(random.nextLong());
            } else if (clazz == short.class || clazz == Short.class) {
                return Short.toString((short) random.nextInt(Short.MAX_VALUE));
            } else if (clazz == byte.class || clazz == Byte.class) {
                return Byte.toString((byte) random.nextInt(Byte.MAX_VALUE));
            } else if (clazz == char.class || clazz == Character.class) {
                return Character.toString((char) (random.nextInt(26) + 'a'));
            } else if (clazz == double.class || clazz == Double.class) {
                return Double.toString(random.nextDouble());
            } else if (clazz == float.class || clazz == Float.class) {
                return Float.toString(random.nextFloat());
            } else if (clazz == boolean.class || clazz == Boolean.class) {
                return Boolean.toString(random.nextBoolean());
            } else if (clazz == String.class) {
                return generateRandomString();
            } else if (clazz == Date.class) {
                return generateRandomDate();
            } else {
                return "Unknown data type: " + className;
            }
        } catch (ClassNotFoundException e) {
            return "Class not found: " + className;
        }
    }
    private static String generateRandomDate() {
        long randomTimeMillis = System.currentTimeMillis() - random.nextLong();
        Date randomDate = new Date(randomTimeMillis);
        return dateFormat.format(String.valueOf(randomDate));
    }

    private static String generateRandomString() {
        int length = random.nextInt(10) + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = (char) (random.nextInt(26) + 'a');
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static String generateRandomDataList(String className) {
        try {
            Class<?> clazz = Class.forName(className);

            if (clazz == int.class || clazz == Integer.class) {
                List<Integer> list = new ArrayList<>();
                int listSize = random.nextInt(10) + 1; // 随机确定 List 的大小
                for (int i = 0; i < listSize; i++) {
                    list.add(random.nextInt());
                }
                return list.toString();
            } else if (clazz == long.class || clazz == Long.class) {
                List<Long> list = new ArrayList<>();
                int listSize = random.nextInt(10) + 1;
                for (int i = 0; i < listSize; i++) {
                    list.add(random.nextLong());
                }
                return list.toString();
            } else if (clazz == short.class || clazz == Short.class) {
                List<Short> list = new ArrayList<>();
                int listSize = random.nextInt(10) + 1;
                for (int i = 0; i < listSize; i++) {
                    list.add((short) random.nextInt(Short.MAX_VALUE));
                }
                return list.toString();
            } else if (clazz == byte.class || clazz == Byte.class) {
                List<Byte> list = new ArrayList<>();
                int listSize = random.nextInt(10) + 1;
                for (int i = 0; i < listSize; i++) {
                    list.add((byte) random.nextInt(Byte.MAX_VALUE));
                }
                return list.toString();
            } else if (clazz == char.class || clazz == Character.class) {
                List<Character> list = new ArrayList<>();
                int listSize = random.nextInt(10) + 1;
                for (int i = 0; i < listSize; i++) {
                    list.add((char) (random.nextInt(26) + 'a'));
                }
                return list.toString();
            } else if (clazz == double.class || clazz == Double.class) {
                List<Double> list = new ArrayList<>();
                int listSize = random.nextInt(10) + 1;
                for (int i = 0; i < listSize; i++) {
                    list.add(random.nextDouble());
                }
                return list.toString();
            } else if (clazz == float.class || clazz == Float.class) {
                List<Float> list = new ArrayList<>();
                int listSize = random.nextInt(10) + 1;
                for (int i = 0; i < listSize; i++) {
                    list.add(random.nextFloat());
                }
                return list.toString();
            } else if (clazz == boolean.class || clazz == Boolean.class) {
                List<Boolean> list = new ArrayList<>();
                int listSize = random.nextInt(10) + 1;
                for (int i = 0; i < listSize; i++) {
                    list.add(random.nextBoolean());
                }
                return list.toString();
            } else if (clazz == String.class) {
                List<String> list = new ArrayList<>();
                int listSize = random.nextInt(10) + 1;
                for (int i = 0; i < listSize; i++) {
                    list.add(generateRandomString());
                }
                return list.toString();
            } else {
                return "Unknown data type: " + className;
            }
        } catch (ClassNotFoundException e) {
            return "Class not found: " + className;
        }
    }
}

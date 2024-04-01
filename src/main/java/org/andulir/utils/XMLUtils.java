package org.andulir.utils;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class XMLUtils {


    /*
    <param>
        <type>
            <name>xxx<name>
            <g>
                <type>
                    <name>xxx<name>
                    <g>
                        ......
                    <g>
                <type>
            <g>
        <type>

        <type>
            <name>xxx<name>
            <a>
                <type>
                    <name>xxx<name>
                    <a>
                        ......
                    <a>
                <type>
                <type>
                    <name>xxx<name>
                    <a>
                        ......
                    <a>
                <type>
            <a>
        <type>
    <param>
     */

    public static void addBasicValue(Element typeMapping,String value) {
        typeMapping.addElement("value").setText(value);
    }

    public static Element addAttributeMapping(Element typeMapping) {
        Element attributeMapping = typeMapping.addElement("attributeMapping");
        return attributeMapping;
    }

    public static Element addGenericsMapping(Element typeMapping) {
        Element genericsMapping = typeMapping.addElement("genericsMapping");
        return genericsMapping;
    }
    public static Element addGenericsMapping(Element typeMapping,String paramType) {
        Element genericsMapping = typeMapping.addElement("genericsMapping");
        Element typeMapping1 = addTypeMapping(genericsMapping, paramType);
        return typeMapping1;
    }
    public static Element addTypeMapping(Element parameterMapping,String paramType) {
        Element typeMapping = parameterMapping.addElement("typeMapping");
        typeMapping.addElement("name").setText(paramType);
        return typeMapping;
    }


    public static Element addParameterMapping(Element methodMapping) {
        Element parameterMapping = methodMapping.addElement("parameterMapping");
        return parameterMapping;
    }

    public static Element addMethodMapping(Element controller,String methodName,String methodStatus) {
        Element methodMapping = controller.addElement("methodMapping");
        methodMapping.addAttribute("name",methodName);
        methodMapping.addAttribute("status",methodStatus);
        return methodMapping;
    }

    public static Element addControllerMapping(Element root,String clazz) {
        Element controllerMapping = root.addElement("controllerMapping");
        controllerMapping.addAttribute("name",clazz);
        return controllerMapping;
    }
    public static Element initXmlFile(Document document,File file) {

        if (file.exists()) {
            log.info("atest.xml文件已存在!");
            return null;
        }
        Element rootElement = document.addElement("aTest");
        writeXML(document,file);
        return rootElement;
    }

    public static void writeXML(Document document,File file) {
        OutputFormat of = OutputFormat.createPrettyPrint();
        of.setEncoding("UTF-8");

        try {
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), of);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

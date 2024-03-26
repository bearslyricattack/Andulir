package org.andulir.dataparser.entity;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class ControllerMapping {
    private String name;
    private List<MethodMapping> methodMappings;  // 修改为 List<MethodMapping>

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "methodmapping")
    public List<MethodMapping> getMethodMappings() {  // 修改方法名和返回类型
        return methodMappings;
    }

    public void setMethodMappings(List<MethodMapping> methodMappings) {  // 修改方法名和参数
        this.methodMappings = methodMappings;
    }
}
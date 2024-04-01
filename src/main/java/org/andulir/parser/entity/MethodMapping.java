package org.andulir.parser.entity;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;


 public class MethodMapping {
    private String name;
    private int status;
    private String isRequest;
    private List<ExampleMapping> examples;

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @XmlElement
    public String getIsRequest() {
        return isRequest;
    }

    public void setIsRequest(String isRequest) {
        this.isRequest = isRequest;
    }

    @XmlElement(name = "examplemapping")
    public List<ExampleMapping> getExamples() {
        return examples;
    }

    public void setExamples(List<ExampleMapping> examples) {
        this.examples = examples;
    }
}
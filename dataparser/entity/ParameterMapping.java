package com.elevator.unit.Andulir.dataparser.entity;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;


public class ParameterMapping {
    private String type;
    private String value;

    @XmlElement
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
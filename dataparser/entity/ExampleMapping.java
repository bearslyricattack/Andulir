package com.elevator.unit.Andulir.dataparser.entity;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class ExampleMapping {
    private List<ParameterMapping> parameterMappings;

    @XmlElement(name = "parametermapping")
    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }
}
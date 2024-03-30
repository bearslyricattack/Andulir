package org.andulir.parser.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ATestEntity {
    private List<ControllerMapping> controllers;

    @XmlElement(name = "controllermapping")
    public List<ControllerMapping> getControllers() {
        return controllers;
    }

    public void setControllers(List<ControllerMapping> controllers) {
        this.controllers = controllers;
    }
}
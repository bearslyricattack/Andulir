package org.andulir;
import org.andulir.config.AndulirProperty;
import org.andulir.dataaccess.InterfaceDataAccess;
import org.andulir.generator.InterfaceDataGenerator;
import org.andulir.parser.InterfaceDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import javax.xml.bind.JAXBException;
import java.lang.reflect.InvocationTargetException;

@SpringBootApplication
public class AndulirApplication implements CommandLineRunner {
    @Autowired
    private InterfaceDataAccess interfaceDataAccess;

    @Autowired
    private AndulirProperty property;
    @Autowired
    private InterfaceDataParser interfaceDataParser;
    @Autowired
    private InterfaceDataGenerator interfaceDataGenerator;
    public static void main(String[] args) {
        SpringApplication.run(AndulirApplication.class,args);
    }

    @Override
    public void run(String... args) throws JAXBException, InvocationTargetException, IllegalAccessException {
        boolean flag = interfaceDataParser.initXML();
        if (flag) {
            interfaceDataParser.conversionInterfaceInformation(property.getScanPackage());
            interfaceDataGenerator.generateRandomData();
        }
//        interfaceDataAccess.test();
    }
}
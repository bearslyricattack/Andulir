package com.elevator.unit.Andulir;

import com.elevator.unit.Andulir.dataaccess.InterfaceDataAccess;
import com.elevator.unit.Andulir.dataparser.InterfaceDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.bind.JAXBException;
import java.lang.reflect.InvocationTargetException;

@SpringBootApplication
public class AndulirTest implements CommandLineRunner {



    @Autowired
    private InterfaceDataAccess interfaceDataAccess;


    public static void main(String[] args) {
        SpringApplication.run(AndulirTest.class, args);
    }

    @Override
    public void run(String... args) throws JAXBException, InvocationTargetException, IllegalAccessException {
        InterfaceDataParser interfaceDataParser = new InterfaceDataParser();
        interfaceDataParser.interfaceFileInitialization();
        interfaceDataParser.conversionInterfaceInformation("com.elevator.unit.controller");
        interfaceDataAccess.test();
    }
}


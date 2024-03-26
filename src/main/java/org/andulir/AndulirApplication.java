package org.andulir;
import org.andulir.dataaccess.InterfaceDataAccess;
import org.andulir.dataparser.InterfaceDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.xml.bind.JAXBException;
import java.lang.reflect.InvocationTargetException;

@SpringBootApplication
@ComponentScan(basePackages ={"${andulir.basePackage}","org.andulir"})
public class AndulirApplication implements CommandLineRunner {
    @Autowired
    private InterfaceDataAccess interfaceDataAccess;
    @Value("${andulir.baseControllerPackage}")
    private String basePackage;

    public static void start(String[] args) {
        SpringApplication.run(AndulirApplication.class,args);
    }

    @Override
    public void run(String... args) throws JAXBException, InvocationTargetException, IllegalAccessException {
        System.out.println(basePackage);
        InterfaceDataParser interfaceDataParser = new InterfaceDataParser();
        interfaceDataParser.interfaceFileInitialization();
        interfaceDataParser.conversionInterfaceInformation(basePackage);
        interfaceDataAccess.test();
    }
}
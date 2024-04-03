package org.andulir;
import org.andulir.access.InterfaceDataAccess;
import org.andulir.config.AndulirProperty;
import org.andulir.generator.InterfaceDataGenerator;
import org.andulir.parser.InterfaceDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AndulirApplication implements CommandLineRunner {
    @Autowired
    private AndulirProperty property;
    @Autowired
    private InterfaceDataParser interfaceDataParser;
    @Autowired
    private InterfaceDataGenerator interfaceDataGenerator;
    @Autowired
    private InterfaceDataAccess interfaceDataAccess;
    public static void start(String[] args) {
        SpringApplication.run(AndulirApplication.class,args);
    }

    @Override
    public void run(String... args) {
        boolean flag = interfaceDataParser.initXML();
        if (flag) {
            interfaceDataParser.conversionInterfaceInformation(property.getScanPackage());
            interfaceDataGenerator.generateRandomData();
            interfaceDataAccess.testMethod();
        }
    }
}
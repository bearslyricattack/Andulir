package org.andulir.config;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.File;

@Configuration
public class AndulirConfig {
    @Bean
    public PodamFactory podamFactory() {
        return new PodamFactoryImpl();
    }

    @Bean
    public Document document() {
        return DocumentHelper.createDocument();
    }

    @Bean
    public File file() {
        return new File("atest.xml");
    }

}

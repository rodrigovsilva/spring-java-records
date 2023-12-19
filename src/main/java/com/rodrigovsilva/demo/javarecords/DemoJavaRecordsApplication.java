package com.rodrigovsilva.demo.javarecords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DemoJavaRecordsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoJavaRecordsApplication.class, args);
    }

}

package com.rodrigovsilva.demo.javarecords.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record ConfigurationRecord(@NotBlank String logLevel, @NotNull Provider provider) {

    public record Provider(@NotNull Silverflow silverflow) {
    }

    public record Silverflow(@NotBlank String url) {
    }
    
    // application-*.yaml
    //  app:
    //      logLevel: DEBUG
    //      provider:
    //          silverflow:
    //              url: https://www.silverflow.com

}







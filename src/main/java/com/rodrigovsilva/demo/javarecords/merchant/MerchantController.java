package com.rodrigovsilva.demo.javarecords.merchant;

import com.rodrigovsilva.demo.javarecords.config.ConfigurationRecord;
import com.rodrigovsilva.demo.javarecords.dto.Merchant;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/merchant")
public class MerchantController {

    private MerchantService merchantService;
    private ConfigurationRecord config;

    public MerchantController(MerchantService merchantService, ConfigurationRecord config) {
        this.merchantService = merchantService;
        this.config = config;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Merchant createMerchant(@Valid @RequestBody Merchant newUser) {

        return merchantService.createMerchant(newUser);
    }

}

package com.rodrigovsilva.demo.javarecords.customer;

import com.rodrigovsilva.demo.javarecords.config.ConfigurationRecord;
import com.rodrigovsilva.demo.javarecords.dto.Customer;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/merchant")
public class CustomerController {

    private CustomerService customerService;
    private ConfigurationRecord config;

    public CustomerController(CustomerService customerService, ConfigurationRecord config) {
        this.customerService = customerService;
        this.config = config;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer createMerchant(@Valid @RequestBody Customer newUser) {

        return customerService.createCustomer(newUser);
    }

}

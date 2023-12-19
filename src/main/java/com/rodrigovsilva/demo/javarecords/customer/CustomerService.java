package com.rodrigovsilva.demo.javarecords.customer;


import com.rodrigovsilva.demo.javarecords.dto.Customer;
import com.rodrigovsilva.demo.javarecords.entity.CustomerEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private CustomerRepository customerRepo;

    public CustomerService(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Transactional
    public Customer createCustomer(Customer user) {
        var entity = new CustomerEntity(user.username(), user.email());
        var saved = customerRepo.save(entity);
        return new Customer(saved.getUsername(), saved.getEmail());
    }


    @Transactional(readOnly = true)
    public List<Customer> getCustomers() {

        return customerRepo.loadCustomers();
    }
}

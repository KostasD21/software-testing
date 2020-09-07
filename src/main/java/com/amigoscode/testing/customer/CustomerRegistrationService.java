package com.amigoscode.testing.customer;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request) throws IllegalStateException {
        //1. Check if phone number exists
        //2. If exists, then check if the customer is the same
        // - 2.1 If yes, then return
        // - 2.2 throw an exception
        //3. Save the customer

    }
}

package com.amigoscode.testing.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request) {
        Optional<Customer> existingCustomer = customerRepository.selectCustomerByPhoneNumber(request.getCustomer().getPhoneNumber());
        if (existingCustomer.isPresent()) {
            if (!existingCustomer.get().getName().equals(request.getCustomer().getName())) {
                throw new IllegalStateException(String.format("The customer with phone number [%s] already exists",request.getCustomer().getPhoneNumber()));
            }
            return;
        }

        if (request.getCustomer().getId() == null) {
            request.getCustomer().setId(UUID.randomUUID());
        }

        customerRepository.save(request.getCustomer());
    }
}

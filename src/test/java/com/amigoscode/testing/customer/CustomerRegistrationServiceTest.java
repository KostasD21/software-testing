package com.amigoscode.testing.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerRegistrationService underTest;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldSaveNewCustomer() {
        //Given a phone number and a customer
        String phoneNumber= "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);

        //Given a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //No customer with phone number
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        //When
        underTest.registerNewCustomer(request);

        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerCaptureValue = customerArgumentCaptor.getValue();
        assertThat(customerCaptureValue).isEqualTo(customer);
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        //Given a phone number and a customer
        String phoneNumber= "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);

        //Given a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //An existing customer is returned
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        //When
        underTest.registerNewCustomer(request);

        //Then
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void itShouldThrowExceptionWhenPhoneNumberIsTaken() {
        //Given a phone number and a customer
        String phoneNumber= "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);
        Customer existingCustomer = new Customer(UUID.randomUUID(), "John", phoneNumber);

        //Given a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //An existing customer is returned
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(existingCustomer));

        //When
        assertThatThrownBy(() -> underTest.registerNewCustomer(request))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining(String.format("The customer with phone number [%s] already exists", phoneNumber));

        //Then
        then(customerRepository).should(never()).save(any(Customer.class));
    }

    @Test
    void itShouldSaveNewCustomerWhenIdIsNull() {
        //Given a phone number and a customer
        String phoneNumber= "000099";
        Customer customer = new Customer(null, "Maryam", phoneNumber);

        //Given a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //No customer with phone number
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        //When
        underTest.registerNewCustomer(request);

        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerCaptureValue = customerArgumentCaptor.getValue();
        assertThat(customerCaptureValue).isEqualToIgnoringGivenFields(customer, "id");
        assertThat(customerCaptureValue.getId()).isNotNull();
    }
}
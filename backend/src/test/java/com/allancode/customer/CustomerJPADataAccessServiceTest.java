package com.allancode.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADataAccessServiceTest {
    private CustomerJPADataAccessService undertest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);// initialise the mock
        undertest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close(); // after each test we have a fresh mock

    }

    @Test
    void itShouldSelectAllCustomers() {
        //Given
        //When
        //Then

    }

    @Test
    void itShouldSelectCustomerById() {
        //Given
        //When
        //Then

    }

    @Test
    void itShouldInsertCustomer() {
        //Given
        //When
        //Then

    }

    @Test
    void itShouldExistsCustomerWithEmail() {
        //Given
        //When
        //Then

    }

    @Test
    void itShouldDeleteCustomerById() {
        //Given
        //When
        //Then

    }

    @Test
    void itShouldExistsCustomerWithId() {
        //Given
        //When
        //Then

    }

    @Test
    void itShouldUpdateCustomer() {
        //Given
        //When
        //Then

    }
}
package com.allancode.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CustomerJPADataAccessServiceTest {
    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);// initialise the mock
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close(); // after each test we have a fresh mock

    }

    @Test
    void itShouldSelectAllCustomers() {
        //When
        underTest.selectAllCustomers();
        //Then
        Mockito.verify(customerRepository)
                .findAll();

    }

    @Test
    void itShouldSelectCustomerById() {
        //Given
        int id = 1;

        //When
        underTest.selectCustomerById(id);

        //Then
        Mockito.verify(customerRepository).findById(id);

    }

    @Test
    void itShouldInsertCustomer() {
        //Given
        Customer customer = new Customer(
                1, "Ali", "ali@gmail.com", 2
        );

        //When
        underTest.insertCustomer(customer);

        //Then
        Mockito.verify(customerRepository).save(customer);

    }

    @Test
    void itShouldExistsCustomerWithEmail() {
        //Given
        String email =  "foo@gmail.com";

        //When
        underTest.existsCustomerWithEmail(email);

        //Then
        Mockito.verify(customerRepository).existsCustomerByEmail(email);

    }

    @Test
    void itShouldDeleteCustomerById() {
        //Given
        int id = 1;

        //When
        underTest.deleteCustomerById(id);

        //Then
        Mockito.verify(customerRepository).deleteById(id);

    }

    @Test
    void itShouldExistsCustomerWithId() {
        //Given
        int id = 1;

        //When
        underTest.existsCustomerWithId(id);

        //Then
        Mockito.verify(customerRepository).existsCustomerById(id);

    }

    @Test
    void itShouldUpdateCustomer() {
        //Given
        Customer customer = new Customer(
                1, "Ali", "ali@gmail.com", 2
        );

        //When
        underTest.updateCustomer(customer);

        //Then
        Mockito.verify(customerRepository).save(customer);


    }
}
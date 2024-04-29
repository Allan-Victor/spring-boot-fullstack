package com.allancode.customer;

import com.allancode.exception.DuplicateResourceException;
import com.allancode.exception.RequestValidationException;
import com.allancode.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private CustomerService underTest;
    @Mock
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @Test
    void itShouldGetAllCustomers() {
        //When
        underTest.getAllCustomers();

        //Then
        verify(customerDAO).selectAllCustomers();

    }

    @Test
    void itShouldGetCustomer() {
        //Given
         int id = 10;
         Customer customer = new Customer(
                 id, "Alex", "alex@gmail.com", 19
         );
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomer(id);

        //Then
        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void itShouldThrowWhenGetCustomerReturnsEmptyOptional() {
        //Given
        int id = 10;
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id does not exist");
    }

    @Test
    void itShouldAddCustomer() {
        //Given
        String email = "alex@gmail.com";
        when(customerDAO.existsCustomerWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex", email, 19);

        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void itShouldThrowWhenEmailExistsWhenAddCustomer() {
        //Given
        String email = "alex@gmail.com";
        when(customerDAO.existsCustomerWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex", email, 19);

        //When
        //Then
        assertThatThrownBy(() ->underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");


        verify(customerDAO, never()).insertCustomer(any());

    }

    @Test
    void itShouldRemoveCustomerById() {
        //Given
        int id = 10;
        Mockito.when(customerDAO.existsCustomerWithId(id)).thenReturn(true);

        //When
        underTest.removeCustomerById(id);
        //Then
        verify(customerDAO).deleteCustomerById(id);

    }

    @Test
    void itShouldThrowWhenRemoveCustomerById() {
        //Given
        int id = 10;
        Mockito.when(customerDAO.existsCustomerWithId(id)).thenReturn(false);

        //When
        //Then
        assertThatThrownBy(() -> underTest.removeCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer does not exist");

        verify(customerDAO ,never()).deleteCustomerById(id);

    }

    @Test
    void itShouldUpdateAllCustomerProperties() {
        //Given
        int id = 10;
        CustomerUpdateRequest request = new CustomerUpdateRequest("Alex", "Alex",  19);

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDAO.existsCustomerWithEmail(request.email())).thenReturn(false);

        //When
        underTest.updateCustomer(id, request);

        //Then
        ArgumentCaptor<Customer> actual = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(actual.capture());
        Customer capturedCustomer = actual.getValue();


        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void itShouldUpdateOnlyCustomerName() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", null, 19
        );
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Jamila", null,  null);


        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //When
        underTest.updateCustomer(id, request);

        //Then
        ArgumentCaptor<Customer> actual = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(actual.capture());
        Customer capturedCustomer = actual.getValue();


        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void itShouldUpdateOnlyCustomerEmail() {
        //Given
        int id = 10;
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null, "Alex@gmail.com",  null);

        Customer customer = new Customer(
                id, "Alex", "eve@gmail.com", 19
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDAO.existsCustomerWithEmail(request.email())).thenReturn(false);


        //When
        underTest.updateCustomer(id, request);

        //Then
        ArgumentCaptor<Customer> actual = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(actual.capture());
        Customer capturedCustomer = actual.getValue();


        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void itShouldUpdateOnlyCustomerAge() {
        //Given
        int id = 10;
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null, null,  20);

        Customer customer = new Customer(
                id, "Alex", "eve@gmail.com", 19
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //When
        underTest.updateCustomer(id, request);

        //Then
        ArgumentCaptor<Customer> actual = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(actual.capture());
        Customer capturedCustomer = actual.getValue();


        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void itShouldThrowWhenUpdateCustomerEmailAlreadyTaken() {
        //Given
        int id = 10;
        String newEmail = "allan@gmail.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Alex", newEmail,  20);

        Customer customer = new Customer(
                id, "Alex", "eve@yahoo.com", 19
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDAO.existsCustomerWithEmail(newEmail)).thenReturn(true);

        //When
        assertThatThrownBy(() ->underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        //Then
        verify(customerDAO, never()).updateCustomer(any());
    }

    @Test
    void itShouldThrowWhenUpdateAllCustomerHasNoChanges() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAge());


        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //When
        assertThatThrownBy(() ->underTest.updateCustomer(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        //Then
        verify(customerDAO, never()).updateCustomer(any());
    }
    }


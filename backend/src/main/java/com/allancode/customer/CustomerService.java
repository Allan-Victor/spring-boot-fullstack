package com.allancode.customer;

import com.allancode.exception.DuplicateResourceException;
import com.allancode.exception.RequestValidationException;
import com.allancode.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers(){
        return customerDAO.selectAllCustomers();
    }


    public Customer getCustomer(Integer id){
        return customerDAO.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFound("customer with id does not exist"));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exists
        if (customerDAO.existsCustomerWithEmail(customerRegistrationRequest.email())){
            throw new DuplicateResourceException("Email already taken");
        }
        //add
        customerDAO.insertCustomer(new Customer(customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()));

    }


    public void removeCustomerById(Integer id){
        if (customerDAO.existsCustomerWithId(id)){
            throw new ResourceNotFound("Customer does not exist");
        }
        customerDAO.deleteCustomerById(id);
    } {
    }

    public void updateCustomer(Integer customerId,
                               CustomerUpdateRequest updateRequest) {
        //TODO: for JPA use .getReferenceById(customerId)
        Customer customer = getCustomer(customerId);

        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())){
            customer.setName((updateRequest.name()));
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())){
            customer.setAge((updateRequest.age()));
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())){
            if(customerDAO.existsCustomerWithEmail(updateRequest.email())){
                throw new DuplicateResourceException("email already taken");
            }
            customer.setEmail((updateRequest.email()));
            changes = true;
        }
        if (!changes){
            throw  new RequestValidationException("no data changes found");
        }
        customerDAO.updateCustomer(customer);
    }
}

package com.allancode.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existsCustomerWithEmail(String email);

    void deleteCustomerById(Integer customerId);

    boolean existsCustomerWithId(Integer id);
    void updateCustomer(Customer update);
}

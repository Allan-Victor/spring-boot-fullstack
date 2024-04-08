package com.allancode.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") Integer customerId){
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request){
        customerService.addCustomer(request);
    }

    @DeleteMapping("/delete/{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer customerId){
        customerService.removeCustomerById(customerId);
    }

    @PutMapping("/update/{customerId}")
    public void updateCustomer(@PathVariable("customerId") Integer customerId,
                               @RequestBody CustomerUpdateRequest updateRequest) {
        customerService.updateCustomer(customerId, updateRequest);

    }

}

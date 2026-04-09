package com.project.orderinventorymanagement.customerservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.orderinventorymanagement.customerservice.entity.Customer;
import com.project.orderinventorymanagement.customerservice.exception.CustomerNotFoundException;
import com.project.orderinventorymanagement.customerservice.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repo;


    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }


    public Customer getCustomer(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }


    public Customer createCustomer(Customer c) {
        return repo.save(c);
    }

 
    @Transactional
    public Customer updateCustomer(Long id, Customer c) {
        Customer existing = getCustomer(id);
        existing.setFullName(c.getFullName());
        existing.setEmailAddress(c.getEmailAddress());
        return repo.save(existing);
    }

   
    @Transactional
    public void deleteCustomer(Long id) {
        repo.deleteById(id);
    }

  
    public boolean validateCustomer(Long id) {
        return repo.existsById(id);
    }

   
    @Transactional
    public void blockCustomer(Long id) {
        Customer c = getCustomer(id);
        c.setStatus("BLOCKED");
        repo.save(c);
    }
    
}
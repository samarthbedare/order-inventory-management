package com.project.orderinventorymanagement.customerservice.service;

import com.project.orderinventorymanagement.customerservice.entity.Customer;
import com.project.orderinventorymanagement.customerservice.exception.*;
import com.project.orderinventorymanagement.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repo;


    public List<Customer> getAllCustomers() {
        List<Customer> customers = repo.findAll();
        if (customers.isEmpty()) {
            throw new CustomerNotFoundException("No customers found in the system");
        }
        return customers;
    }


    public Customer getCustomer(Integer id) {
        if (id == null || id <= 0) {
            throw new InvalidCustomerDataException("Customer ID must be a positive number");
        }
        return repo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with ID: " + id
                ));
    }


    public Customer createCustomer(Customer c) {

        if (c.getFullName() == null || c.getFullName().isBlank()) {
            throw new InvalidCustomerDataException("Full name cannot be empty");
        }
        if (c.getEmailAddress() == null || c.getEmailAddress().isBlank()) {
            throw new InvalidCustomerDataException("Email address cannot be empty");
        }


        boolean emailExists = repo.findAll()
                .stream()
                .anyMatch(existing -> existing.getEmailAddress()
                        .equalsIgnoreCase(c.getEmailAddress()));

        if (emailExists) {
            throw new CustomerAlreadyExistsException(
                    "Customer with email " + c.getEmailAddress() + " already exists"
            );
        }

        return repo.save(c);
    }


    @Transactional
    public Customer updateCustomer(Integer id, Customer c) {

        if (id == null || id <= 0) {
            throw new InvalidCustomerDataException("Customer ID must be a positive number");
        }


        if (c.getFullName() == null || c.getFullName().isBlank()) {
            throw new CustomerUpdateException("Full name cannot be empty for update");
        }
        if (c.getEmailAddress() == null || c.getEmailAddress().isBlank()) {
            throw new CustomerUpdateException("Email address cannot be empty for update");
        }

        Customer existing = repo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Cannot update — Customer not found with ID: " + id
                ));

        existing.setFullName(c.getFullName());
        existing.setEmailAddress(c.getEmailAddress());

        try {
            return repo.save(existing);
        } catch (Exception e) {
            throw new CustomerUpdateException(
                    "Failed to update customer with ID: " + id + ". " + e.getMessage()
            );
        }
    }


    @Transactional
    public void deleteCustomer(Integer id) {
        if (id == null || id <= 0) {
            throw new InvalidCustomerDataException("Customer ID must be a positive number");
        }

        if (!repo.existsById(id)) {
            throw new CustomerNotFoundException(
                    "Cannot delete — Customer not found with ID: " + id
            );
        }

        try {
            repo.deleteById(id);
        } catch (Exception e) {
            throw new CustomerDeleteException(
                    "Failed to delete customer with ID: " + id + ". " + e.getMessage()
            );
        }
    }

    public boolean validateCustomer(Integer id) {
        if (id == null || id <= 0) {
            throw new InvalidCustomerDataException(
                    "Customer ID must be a positive number for validation"
            );
        }
        return repo.existsById(id);
    }
}
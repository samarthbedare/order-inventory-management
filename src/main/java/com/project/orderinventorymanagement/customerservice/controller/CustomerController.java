package com.project.orderinventorymanagement.customerservice.controller;

import com.project.orderinventorymanagement.customerservice.dto.CustomerDTO;
import com.project.orderinventorymanagement.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return ResponseEntity.ok(service.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getCustomer(id));
    }

    @GetMapping("/email/{email:.+}")
    public ResponseEntity<CustomerDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getCustomerByEmail(email));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> create(@RequestBody CustomerDTO c) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCustomer(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Integer id, @RequestBody CustomerDTO c) {
        return ResponseEntity.ok(service.updateCustomer(id, c));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validate/{id}")
    public ResponseEntity<Boolean> validate(@PathVariable Integer id) {
        return ResponseEntity.ok(service.validateCustomer(id));
    }
}

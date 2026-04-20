package com.project.orderinventorymanagement.customerservice.service;

import com.project.orderinventorymanagement.customerservice.dto.CustomerDTO;
import com.project.orderinventorymanagement.customerservice.entity.Customer;
import com.project.orderinventorymanagement.customerservice.exception.*;
import com.project.orderinventorymanagement.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository repo;

    @InjectMocks
    private CustomerService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomers_ReturnsList() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setFullName("John Doe");
        customer.setEmailAddress("john@example.com");

        when(repo.findAll()).thenReturn(List.of(customer));

        List<CustomerDTO> result = service.getAllCustomers();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFullName()).isEqualTo("John Doe");
    }

    @Test
    void getAllCustomers_Empty_ThrowsException() {
        when(repo.findAll()).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> service.getAllCustomers())
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void getCustomer_ValidId_ReturnsDTO() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setFullName("John Doe");
        customer.setEmailAddress("john@example.com");
        when(repo.findById(1)).thenReturn(Optional.of(customer));

        CustomerDTO result = service.getCustomer(1);
        assertThat(result.getCustomerId()).isEqualTo(1);
    }

    @Test
    void getCustomer_InvalidId_ThrowsException() {
        assertThatThrownBy(() -> service.getCustomer(0))
                .isInstanceOf(InvalidCustomerDataException.class);
    }

    @Test
    void getCustomer_NotFound_ThrowsException() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getCustomer(1))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void getCustomerByEmail_Valid_ReturnsDTO() {
        Customer customer = new Customer();
        customer.setEmailAddress("test@test.com");
        customer.setFullName("Tester");
        when(repo.findByEmailAddress("test@test.com")).thenReturn(Optional.of(customer));

        CustomerDTO result = service.getCustomerByEmail("test@test.com");
        assertThat(result.getEmailAddress()).isEqualTo("test@test.com");
    }

    @Test
    void getCustomerByEmail_Blank_ThrowsException() {
        assertThatThrownBy(() -> service.getCustomerByEmail(""))
                .isInstanceOf(InvalidCustomerDataException.class);
    }

    @Test
    void createCustomer_Valid_ReturnsDTO() {
        CustomerDTO dto = new CustomerDTO();
        dto.setFullName("Jane");
        dto.setEmailAddress("jane@example.com");

        when(repo.existsByEmailAddress(anyString())).thenReturn(false);
        when(repo.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerDTO result = service.createCustomer(dto);
        assertThat(result.getFullName()).isEqualTo("Jane");
        verify(repo, times(1)).save(any());
    }

    @Test
    void createCustomer_MissingName_ThrowsException() {
        CustomerDTO dto = new CustomerDTO();
        dto.setEmailAddress("jane@example.com");
        assertThatThrownBy(() -> service.createCustomer(dto))
                .isInstanceOf(InvalidCustomerDataException.class);
    }

    @Test
    void createCustomer_AlreadyExists_ThrowsException() {
        CustomerDTO dto = new CustomerDTO();
        dto.setFullName("Jane");
        dto.setEmailAddress("jane@example.com");

        when(repo.existsByEmailAddress("jane@example.com")).thenReturn(true);
        assertThatThrownBy(() -> service.createCustomer(dto))
                .isInstanceOf(CustomerAlreadyExistsException.class);
    }

    @Test
    void updateCustomer_Valid_ReturnsDTO() {
        Customer existing = new Customer();
        existing.setCustomerId(1);
        CustomerDTO updateInfo = new CustomerDTO();
        updateInfo.setFullName("New Name");
        updateInfo.setEmailAddress("new@example.com");

        when(repo.findById(1)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        CustomerDTO result = service.updateCustomer(1, updateInfo);
        assertThat(result.getFullName()).isEqualTo("New Name");
    }

    @Test
    void updateCustomer_UpdateFails_ThrowsException() {
        Customer existing = new Customer();
        CustomerDTO updateInfo = new CustomerDTO();
        updateInfo.setFullName("New Name");
        updateInfo.setEmailAddress("new@example.com");

        when(repo.findById(1)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenThrow(new RuntimeException("DB Error"));

        assertThatThrownBy(() -> service.updateCustomer(1, updateInfo))
                .isInstanceOf(CustomerUpdateException.class);
    }

    @Test
    void deleteCustomer_Valid() {
        when(repo.existsById(1)).thenReturn(true);
        service.deleteCustomer(1);
        verify(repo, times(1)).deleteById(1);
    }

    @Test
    void deleteCustomer_NotFound_ThrowsException() {
        when(repo.existsById(1)).thenReturn(false);
        assertThatThrownBy(() -> service.deleteCustomer(1))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void deleteCustomer_Fails_ThrowsException() {
        when(repo.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("DB Error")).when(repo).deleteById(1);
        assertThatThrownBy(() -> service.deleteCustomer(1))
                .isInstanceOf(CustomerDeleteException.class);
    }

    @Test
    void validateCustomer_ReturnsTrue() {
        when(repo.existsById(1)).thenReturn(true);
        boolean result = service.validateCustomer(1);
        assertThat(result).isTrue();
    }
}

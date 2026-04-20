package com.project.orderinventorymanagement.shippingservice.service;

import com.project.orderinventorymanagement.customerservice.entity.Customer;
import com.project.orderinventorymanagement.customerservice.exception.CustomerNotFoundException;
import com.project.orderinventorymanagement.customerservice.repository.CustomerRepository;
import com.project.orderinventorymanagement.shippingservice.dto.ShipmentRequestDTO;
import com.project.orderinventorymanagement.shippingservice.dto.ShipmentResponseDTO;
import com.project.orderinventorymanagement.shippingservice.entity.Shipment;
import com.project.orderinventorymanagement.shippingservice.entity.ShipmentStatus;
import com.project.orderinventorymanagement.shippingservice.exception.ShipmentNotFoundException;
import com.project.orderinventorymanagement.shippingservice.repository.ShipmentRepository;
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
import static org.mockito.Mockito.*;

class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShipment_FromDTO_Success() {
        ShipmentRequestDTO dto = new ShipmentRequestDTO();
        dto.setCustomerId(1);
        Customer customer = new Customer();
        customer.setCustomerId(1);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(shipmentRepository.save(any())).thenAnswer(i -> {
            Shipment s = i.getArgument(0);
            s.setShipmentId(1);
            return s;
        });

        ShipmentResponseDTO result = shipmentService.createShipment(dto);
        assertThat(result.getShipmentId()).isEqualTo(1);
    }

    @Test
    void createShipment_CustomerNotFound_ThrowsException() {
        ShipmentRequestDTO dto = new ShipmentRequestDTO();
        dto.setCustomerId(1);
        when(customerRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> shipmentService.createShipment(dto))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void getAllShipments_Empty_ThrowsException() {
        when(shipmentRepository.findAll()).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> shipmentService.getAllShipments())
                .isInstanceOf(ShipmentNotFoundException.class);
    }

    @Test
    void getShipmentsByCustomer_Success() {
        when(customerRepository.existsById(1)).thenReturn(true);
        Shipment shipment = new Shipment();
        Customer customer = new Customer();
        customer.setCustomerId(1);
        shipment.setCustomer(customer);
        when(shipmentRepository.findByCustomerCustomerId(1)).thenReturn(List.of(shipment));
        assertThat(shipmentService.getShipmentsByCustomer(1)).hasSize(1);
    }

    @Test
    void getShipmentById_Success() {
        Shipment shipment = new Shipment();
        shipment.setShipmentId(1);
        Customer customer = new Customer();
        customer.setCustomerId(1);
        shipment.setCustomer(customer);
        when(shipmentRepository.findById(1)).thenReturn(Optional.of(shipment));

        ShipmentResponseDTO result = shipmentService.getShipmentById(1);
        assertThat(result.getShipmentId()).isEqualTo(1);
    }

    @Test
    void updateStatus_Success() {
        Shipment shipment = new Shipment();
        shipment.setShipmentId(1);
        Customer customer = new Customer();
        customer.setCustomerId(1);
        shipment.setCustomer(customer);
        when(shipmentRepository.findById(1)).thenReturn(Optional.of(shipment));
        when(shipmentRepository.save(any())).thenReturn(shipment);

        ShipmentResponseDTO result = shipmentService.updateStatus(1, ShipmentStatus.SHIPPED);
        assertThat(result.getShipmentStatus()).isEqualTo(ShipmentStatus.SHIPPED);
    }

    @Test
    void deleteShipment_Success() {
        when(shipmentRepository.existsById(1)).thenReturn(true);
        shipmentService.deleteShipment(1);
        verify(shipmentRepository).deleteById(1);
    }
}

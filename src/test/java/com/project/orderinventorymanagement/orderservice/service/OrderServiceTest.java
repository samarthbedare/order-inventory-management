package com.project.orderinventorymanagement.orderservice.service;

import com.project.orderinventorymanagement.customerservice.entity.Customer;
import com.project.orderinventorymanagement.customerservice.repository.CustomerRepository;
import com.project.orderinventorymanagement.orderservice.dto.OrderItemRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderResponse;
import com.project.orderinventorymanagement.orderservice.entity.Order;
import com.project.orderinventorymanagement.orderservice.entity.OrderItem;
import com.project.orderinventorymanagement.orderservice.entity.OrderStatus;
import com.project.orderinventorymanagement.orderservice.exception.OrderNotFoundException;
import com.project.orderinventorymanagement.orderservice.repository.OrderItemRepository;
import com.project.orderinventorymanagement.orderservice.repository.OrderRepository;
import com.project.orderinventorymanagement.shippingservice.entity.Shipment;
import com.project.orderinventorymanagement.shippingservice.service.ShipmentService;
import com.project.orderinventorymanagement.storeservice.service.InventoryService;
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

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private InventoryService inventoryService;
    @Mock
    private ShipmentService shipmentService;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_Success() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1);
        request.setStoreId(1);
        request.setDeliveryAddress("Home");
        OrderItemRequest itemReq = new OrderItemRequest();
        itemReq.setProductId(1);
        itemReq.setQuantity(2);
        request.setItems(List.of(itemReq));

        Customer customer = new Customer();
        customer.setCustomerId(1);
        Shipment shipment = new Shipment();
        shipment.setShipmentId(1);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(shipmentService.createShipment(any(Shipment.class))).thenReturn(shipment);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order o = i.getArgument(0);
            o.setOrderId(1);
            return o;
        });

        List<OrderResponse> result = orderService.createOrder(request);

        assertThat(result).hasSize(1);
        verify(inventoryService).reduceStock(any());
        verify(orderRepository).save(any());
    }

    @Test
    void createOrder_MissingCustomerId_ThrowsException() {
        OrderRequest request = new OrderRequest();
        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getOrderById_Success() {
        Order order = new Order();
        order.setOrderId(1);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        OrderResponse result = orderService.getOrderById(1);
        assertThat(result.getOrderId()).isEqualTo(1);
    }

    @Test
    void getOrderById_NotFound_ThrowsException() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.getOrderById(1))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void getOrdersByCustomer_Success() {
        Order order = new Order();
        when(orderRepository.findByCustomerId(1)).thenReturn(List.of(order));
        
        List<OrderResponse> result = orderService.getOrdersByCustomer(1);
        assertThat(result).hasSize(1);
    }

    @Test
    void getOrdersByCustomer_Empty_ThrowsException() {
        when(orderRepository.findByCustomerId(1)).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> orderService.getOrdersByCustomer(1))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void getAllOrders_Empty_ThrowsException() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> orderService.getAllOrders())
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void getOrdersByStore_Success() {
        Order order = new Order();
        when(orderRepository.findByStoreId(1)).thenReturn(List.of(order));
        assertThat(orderService.getOrdersByStore(1)).hasSize(1);
    }

    @Test
    void updateStatus_Success() {
        Order order = new Order();
        order.setOrderId(1);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        OrderResponse result = orderService.updateStatus(1, "COMPLETE");
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COMPLETE);
    }

    @Test
    void updateStatus_InvalidStatus_ThrowsException() {
        assertThatThrownBy(() -> orderService.updateStatus(1, "INVALID"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteOrder_Success() {
        when(orderRepository.existsById(1)).thenReturn(true);
        boolean result = orderService.deleteOrder(1);
        assertThat(result).isTrue();
        verify(orderRepository).deleteById(1);
    }

    @Test
    void linkShipment_Success() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        order.setItem(item);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        orderService.linkShipment(1, 100);
        assertThat(item.getShipmentId()).isEqualTo(100);
        verify(orderItemRepository).save(item);
    }
}

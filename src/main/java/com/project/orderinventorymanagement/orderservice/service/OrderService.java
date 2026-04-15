package com.project.orderinventorymanagement.orderservice.service;

import com.project.orderinventorymanagement.orderservice.dto.OrderItemRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderResponse;
import com.project.orderinventorymanagement.orderservice.exception.OrderNotFoundException;
import com.project.orderinventorymanagement.orderservice.entity.Order;
import com.project.orderinventorymanagement.orderservice.entity.OrderItem;
import com.project.orderinventorymanagement.orderservice.entity.OrderStatus;
import com.project.orderinventorymanagement.orderservice.repository.OrderItemRepository;
import com.project.orderinventorymanagement.orderservice.repository.OrderRepository;
import com.project.orderinventorymanagement.storeservice.dto.InventoryDTO;
import com.project.orderinventorymanagement.storeservice.service.InventoryService;
import com.project.orderinventorymanagement.shippingservice.service.ShipmentService;
import com.project.orderinventorymanagement.shippingservice.entity.Shipment;
import com.project.orderinventorymanagement.customerservice.repository.CustomerRepository;
import com.project.orderinventorymanagement.customerservice.entity.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryService inventoryService;
    private final ShipmentService shipmentService;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, 
                        InventoryService inventoryService, ShipmentService shipmentService, 
                        CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.inventoryService = inventoryService;
        this.shipmentService = shipmentService;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public List<OrderResponse> createOrder(OrderRequest request) {
        if (request.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID must not be null");
        }
        if (request.getStoreId() == null) {
            throw new IllegalArgumentException("Store ID must not be null");
        }
        if (request.getDeliveryAddress() == null || request.getDeliveryAddress().isBlank()) {
            throw new IllegalArgumentException("Delivery Address must not be null or empty");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        // Fetch Customer for Shipment
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found for id: " + request.getCustomerId()));

        // Create a single Shipment for this Order Checkout
        Shipment shipment = new Shipment();
        shipment.setCustomer(customer);
        shipment.setStoreId(request.getStoreId());
        shipment.setDeliveryAddress(request.getDeliveryAddress());
        Shipment savedShipment = shipmentService.createShipment(shipment);

        List<OrderResponse> createdOrders = new ArrayList<>();

        for (OrderItemRequest itemReq : request.getItems()) {
            // Check & Reduce Inventory
            InventoryDTO inventoryDTO = new InventoryDTO();
            inventoryDTO.setStoreId(request.getStoreId());
            inventoryDTO.setProductId(itemReq.getProductId());
            inventoryDTO.setQuantity(itemReq.getQuantity());
            inventoryService.reduceStock(inventoryDTO);

            // Create separate Order for the single item to satisfy DB constraints
            Order order = new Order();
            order.setCustomerId(request.getCustomerId());
            order.setStoreId(request.getStoreId());
            order.setOrderTms(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.OPEN);

            OrderItem item = new OrderItem();
            item.setLineItemId(1);
            item.setProductId(itemReq.getProductId());
            item.setUnitPrice(itemReq.getUnitPrice());
            item.setQuantity(itemReq.getQuantity());
            item.setShipmentId(savedShipment.getShipmentId()); // Automatically link shipment

            order.setItem(item);

            Order savedOrder = orderRepository.save(order);
            createdOrders.add(toResponse(savedOrder));
        }

        return createdOrders;
    }

    public OrderResponse getOrderById(Integer id) {
        return orderRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    public List<OrderResponse> getOrdersByCustomer(Integer customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No orders found for customer id: " + customerId);
        }
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            responses.add(toResponse(order));
        }
        return responses;
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No orders found in the system");
        }
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            responses.add(toResponse(order));
        }
        return responses;
    }

    public List<OrderResponse> getOrdersByStore(Integer storeId) {
        List<Order> orders = orderRepository.findByStoreId(storeId);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No orders found for store id: " + storeId);
        }
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            responses.add(toResponse(order));
        }
        return responses;
    }

    @Transactional
    public OrderResponse updateStatus(Integer id, String newStatus) {

        if (newStatus == null || newStatus.isBlank()) {
            throw new IllegalArgumentException("Order status must not be null or empty");
        }
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }

        return orderRepository.findById(id)
                .map(order -> {
                    order.setOrderStatus(status);
                    return toResponse(orderRepository.save(order));
                })
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional
    public boolean deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
        return true;
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderTms(order.getOrderTms());
        response.setCustomerId(order.getCustomerId());
        response.setOrderStatus(order.getOrderStatus());
        response.setStoreId(order.getStoreId());

        OrderItem item = order.getItem();
        if (item != null) {
            OrderResponse.ItemDetail detail = new OrderResponse.ItemDetail();
            detail.setOrderId(item.getOrderId());
            detail.setLineItemId(item.getLineItemId());
            detail.setProductId(item.getProductId());
            detail.setUnitPrice(item.getUnitPrice());
            detail.setQuantity(item.getQuantity());
            detail.setShipmentId(item.getShipmentId());
            response.setItem(detail);
        }
        return response;
    }

    @Transactional
    public OrderResponse linkShipment(Integer orderId, Integer shipmentId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    OrderItem item = order.getItem();
                    if (item != null) {
                        item.setShipmentId(shipmentId);
                        orderItemRepository.save(item);
                    }
                    return toResponse(order);
                })
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}

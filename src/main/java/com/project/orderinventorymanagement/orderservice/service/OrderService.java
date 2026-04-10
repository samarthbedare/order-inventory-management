package com.project.orderinventorymanagement.orderservice.service;

import com.project.orderinventorymanagement.orderservice.dto.OrderItemRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderResponse;
import com.project.orderinventorymanagement.orderservice.model.Order;
import com.project.orderinventorymanagement.orderservice.model.OrderItem;
import com.project.orderinventorymanagement.orderservice.model.OrderStatus;
import com.project.orderinventorymanagement.orderservice.repository.OrderItemRepository;
import com.project.orderinventorymanagement.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        // Step 1: Save Order first (without items)
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setStoreId(request.getStoreId());
        order.setOrderTms(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.OPEN);
        order.setOrderItems(new ArrayList<>());

        Order savedOrder = orderRepository.save(order);
        Integer generatedOrderId = savedOrder.getOrderId();

        // Step 2: Save each OrderItem separately with the generated order_id
        List<OrderItem> savedItems = new ArrayList<>();
        if (request.getItems() != null) {
            for (OrderItemRequest itemReq : request.getItems()) {
                OrderItem item = new OrderItem();
                item.setOrderId(generatedOrderId);  // manually set FK = PK
                item.setLineItemId(itemReq.getLineItemId());
                item.setProductId(itemReq.getProductId());
                item.setUnitPrice(itemReq.getUnitPrice());
                item.setQuantity(itemReq.getQuantity());
                savedItems.add(orderItemRepository.save(item));
            }
        }

        savedOrder.setOrderItems(savedItems);
        return toResponse(savedOrder);
    }

    public Optional<OrderResponse> getOrderById(Integer id) {
        return orderRepository.findById(id).map(this::toResponse);
    }

    public List<OrderResponse> getOrdersByCustomer(Integer customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            responses.add(toResponse(order));
        }
        return responses;
    }

    @Transactional
    public Optional<OrderResponse> updateStatus(Integer id, String newStatus) {
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }

        return orderRepository.findById(id).map(order -> {
            order.setOrderStatus(status);
            Order updated = orderRepository.save(order);
            return toResponse(updated);
        });
    }

    @Transactional
    public boolean deleteOrder(Integer id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderTms(order.getOrderTms());
        response.setCustomerId(order.getCustomerId());
        response.setOrderStatus(order.getOrderStatus());
        response.setStoreId(order.getStoreId());

        List<OrderResponse.ItemDetail> itemDetails = new ArrayList<>();
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                OrderResponse.ItemDetail detail = new OrderResponse.ItemDetail();
                detail.setOrderId(item.getOrderId());
                detail.setLineItemId(item.getLineItemId());
                detail.setProductId(item.getProductId());
                detail.setUnitPrice(item.getUnitPrice());
                detail.setQuantity(item.getQuantity());
                detail.setShipmentId(item.getShipmentId());
                itemDetails.add(detail);
            }
        }
        response.setItems(itemDetails);
        return response;
    }

    @Transactional
    public Optional<OrderResponse> linkShipment(Integer orderId, Integer shipmentId) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    item.setShipmentId(shipmentId);
                    orderItemRepository.save(item);
                }
            }
            return toResponse(order);
        });
    }
}
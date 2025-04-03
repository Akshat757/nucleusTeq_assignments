package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.TopRestaurantDTO;
import com.example.restaurantmanagement.model.*;
import com.example.restaurantmanagement.repository.*;
import com.example.restaurantmanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private RestaurantRepository RestaurantRepository;

    @Override
    public Orders placeOrder(Long customerId) {
        // Fetch customer (ensure the role is CUSTOMER)
        app_user customer = userRepository.findById(customerId)
                .filter(user -> "CUSTOMER".equals(user.getRole()))
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Get cart items for the customer
        List<Cart> cartItems = cartRepository.findByCustomer_Id(customerId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot place an order.");
        }

        // Check that all items are from the same restaurant
        Restaurant restaurant = cartItems.get(0).getMenuItem().getRestaurant();
        boolean allSameRestaurant = cartItems.stream()
                .allMatch(item -> item.getMenuItem().getRestaurant().equals(restaurant));
        if (!allSameRestaurant) {
            throw new RuntimeException("All items in the cart must be from the same restaurant.");
        }

        // Calculate total amount
        double total = cartItems.stream()
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();

        if (customer.getWalletBalance() < total) {
            throw new RuntimeException("Insufficient wallet balance.");
        }

        // Deduct wallet balance from customer & save immediately
        customer.setWalletBalance(customer.getWalletBalance() - total);
        userRepository.save(customer);  // ✅ Ensure balance is deducted in DB

        // Create new order (WITHOUT setting order details yet)
        Orders order = new Orders();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(total);
        order.setStatus("Pending");

        // Save order & flush to obtain order ID
        Orders savedOrder = orderRepository.saveAndFlush(order);

        // Create order details for each cart item
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        for (Cart cart : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(savedOrder);
            orderDetail.setMenuItem(cart.getMenuItem());
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setPrice(cart.getMenuItem().getPrice() * cart.getQuantity());
            orderDetailsList.add(orderDetail);
        }

        // Save order details
        orderDetailRepository.saveAll(orderDetailsList);

        // Update order with order details and remaining balance
        savedOrder.setOrderDetails(orderDetailsList);
        savedOrder.setRemainingWalletBalance(customer.getWalletBalance());

        // Clear the customer's cart (Only after everything is saved successfully)
        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }

    @Override
    public List<Orders> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomer_Id(customerId);
    }

    @Override
    public Orders getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Orders> getOrdersByOwner(Long ownerId) {
        return orderRepository.findByRestaurantOwnerId(ownerId);
    }

    public List<TopRestaurantDTO> getTopRestaurantsByOwner(Long ownerId) {
        Pageable topThree = PageRequest.of(0, 3); // Limit to top 3 restaurants
        List<Object[]> results = orderRepository.findTopRestaurantsByOwner(ownerId, topThree);

        return results.stream()
                .map(obj -> new TopRestaurantDTO((Long) obj[0], (String) obj[1], (Long) obj[2]))
                .collect(Collectors.toList());
    }

}

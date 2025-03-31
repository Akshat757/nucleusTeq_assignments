package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.*;
import com.example.restaurantmanagement.repository.OrderDetailRepository;
import com.example.restaurantmanagement.repository.OrderRepository;
import com.example.restaurantmanagement.repository.CartRepository;
import com.example.restaurantmanagement.repository.UserRepository;
import com.example.restaurantmanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Order placeOrder(Long customerId) {
        User customer = userRepository.findById(customerId)
                .filter(user -> "CUSTOMER".equals(user.getRole()))
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Cart> cartItems = cartRepository.findByCustomer_Id(customerId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot place an order.");
        }

        // Ensure that all cart items belong to the same restaurant if that's required by your business rules
        Restaurant restaurant = cartItems.get(0).getMenuItem().getRestaurant();
        boolean allSameRestaurant = cartItems.stream()
                .allMatch(item -> item.getMenuItem().getRestaurant().equals(restaurant));
        if (!allSameRestaurant) {
            throw new RuntimeException("All items in the cart must be from the same restaurant.");
        }

        double total = cartItems.stream()
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();

        if (customer.getWalletBalance() < total) {
            throw new RuntimeException("Insufficient wallet balance.");
        }

        // Create a new order
        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(total);
        order.setStatus("Pending");

        // Deduct wallet balance
        customer.setWalletBalance(customer.getWalletBalance() - total);
        userRepository.save(customer);

        // Save order and flush immediately to get a valid order id
        Order savedOrder = orderRepository.saveAndFlush(order);

        // Create order details for each cart item
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        for (Cart cart : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(savedOrder);  // use savedOrder which now has an ID
            orderDetail.setMenuItem(cart.getMenuItem());
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setPrice(cart.getMenuItem().getPrice() * cart.getQuantity());
            orderDetailsList.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetailsList);
        savedOrder.setOrderDetails(orderDetailsList);

        // Set remaining wallet balance in the order (using transient field)
        savedOrder.setRemainingWalletBalance(customer.getWalletBalance());

        // Clear the cart for this customer
        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }




    @Override
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomer_Id(customerId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}

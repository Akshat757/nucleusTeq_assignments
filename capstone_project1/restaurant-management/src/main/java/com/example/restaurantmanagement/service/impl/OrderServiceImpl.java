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

/**
 * Implementation of the OrderService interface to manage orders in the system.
 * This class handles placing orders, retrieving orders, and analyzing top restaurants.
 */
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

    /**
     * Places an order for a customer, including validation of cart contents, wallet balance,
     * and creation of the order and its details.
     *
     * @param customerId the ID of the customer placing the order
     * @return the placed order with order details
     * @throws RuntimeException if the cart is empty, contains items from multiple restaurants,
     *                          or the customer has insufficient wallet balance
     */
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

    /**
     * Retrieves a list of orders placed by a specific customer.
     *
     * @param customerId the ID of the customer
     * @return a list of orders placed by the customer
     */
    @Override
    public List<Orders> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomer_Id(customerId);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order
     * @return the order with the specified ID
     * @throws RuntimeException if the order is not found
     */
    @Override
    public Orders getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /**
     * Retrieves a list of orders for a restaurant owner.
     *
     * @param ownerId the ID of the restaurant owner
     * @return a list of orders for the owner's restaurant(s)
     */
    @Override
    public List<Orders> getOrdersByOwner(Long ownerId) {
        return orderRepository.findByRestaurantOwnerId(ownerId);
    }

    /**
     * Retrieves the top 3 restaurants by order volume for a specific restaurant owner.
     *
     * @param ownerId the ID of the restaurant owner
     * @return a list of the top 3 restaurants, including their IDs, names, and order counts
     */
    public List<TopRestaurantDTO> getTopRestaurantsByOwner(Long ownerId) {
        Pageable topThree = PageRequest.of(0, 3); // Limit to top 3 restaurants
        List<Object[]> results = orderRepository.findTopRestaurantsByOwner(ownerId, topThree);

        return results.stream()
                .map(obj -> new TopRestaurantDTO((Long) obj[0], (String) obj[1], (Long) obj[2]))
                .collect(Collectors.toList());
    }
}

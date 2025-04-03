# Restaurant Management System

## Overview

The Restaurant Management System (RMS) is an online platform designed to connect customers with restaurant owners. This web application allows restaurant owners to manage their restaurants, menus, and orders, while customers can browse restaurants, place orders, and track their order history. The system includes robust authentication, cart management, order processing, and real-time data display through a responsive user interface.

## Features

### For Customers

- **User Registration & Login**: Simple registration and login functionality using email and password.
- **Restaurant & Menu Browsing**: Search for restaurants and view detailed menu items with images.
- **Cart Management**: Add items to the cart, update quantities, remove items, and view total order amount.
- **Order Placement & Tracking**: Place orders and view order history along with current order status.
- **Wallet Integration**: Customers have a wallet that automatically deducts order totals and shows remaining balance.

### For Restaurant Owners

- **Dashboard**: Role-based dashboard displaying total restaurants managed, total orders received, and total revenue.
- **Restaurant Management**: Add, edit, and delete restaurants. Restaurants are linked to the owner.
- **Menu Management**: Manage menu items (add, update, delete) for each restaurant.
- **Order Management**: View orders placed for their restaurants, including order details and customer information.

## Technology Stack

- **Backend**: Spring Boot (Java 17), Hibernate, PostgreSQL
- **Frontend**: HTML, CSS, JavaScript, Bootstrap, jQuery, SweetAlert2
- **API Integration**: RESTful API endpoints for user, restaurant, menu, cart, and order management

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

// const API_BASE = "http://localhost:8081/api";
//
// // ------------------------------
// // Helper Functions
// // ------------------------------
//
// // Get customer ID from localStorage (default to 1)
// function getCustomerId() {
//     return localStorage.getItem("customerId") || 1;
// }
//
// // Update wallet balance
// function updateWalletBalance() {
//     fetch(`${API_BASE}/wallet/customer/${getCustomerId()}`)
//         .then(response => response.json())
//         .then(balance => {
//             document.getElementById("walletBalance").innerText = balance.toFixed(2);
//         })
//         .catch(error => console.error("Error updating wallet balance:", error));
// }
//
// // ------------------------------
// // Document Ready: Initialize UI
// // ------------------------------
// $(document).ready(function () {
//     $('.animate__animated').addClass('animate__fadeInUp');
//     loadRestaurants();
//     loadCart();
//     loadOrderHistory();
//     updateWalletBalance();
// });
//
// // ------------------------------
// // Restaurant Functions
// // ------------------------------
// function loadRestaurants() {
//     fetch(`${API_BASE}/restaurants`)
//         .then(response => response.json())
//         .then(data => renderRestaurantCards(data))
//         .catch(error => console.error("Error loading restaurants:", error));
// }
//
// function renderRestaurantCards(data) {
//     let html = "";
//     if (data.length === 0) {
//         html = "<p class='lead'>No restaurants available.</p>";
//     } else {
//         data.forEach(restaurant => {
//             html += `
//                 <div class="col-md-6 col-lg-4 mb-4">
//                     <div class="card restaurant-card animate__animated animate__fadeIn">
//                         <img src="${restaurant.imageUrl || 'images/default-restaurant.jpg'}" class="card-img-top" alt="${restaurant.name}">
//                         <div class="card-body">
//                             <h5 class="card-title">${restaurant.name}</h5>
//                             <p class="card-text">${restaurant.location}</p>
//                             <button class="btn btn-primary btn-custom" onclick="viewMenu(${restaurant.id})">View Menu</button>
//                         </div>
//                     </div>
//                 </div>
//             `;
//         });
//     }
//     document.getElementById("restaurantList").innerHTML = html;
// }
//
// // ------------------------------
// // Search Functionality
// // ------------------------------
// function performSearch() {
//     const query = ('#searchQuery').val().toLowerCase();
//     fetch(`${API_BASE}/restaurants/search?query=${query}`)
//         .then(response => response.json())
//         .then(data => renderRestaurantCards(data))
//         .catch(error => console.error("Error during search:", error));
// }
//
// // ------------------------------
// // Menu Functions
// // ------------------------------
// function viewMenu(restaurantId) {
//     fetch(`${API_BASE}/restaurants/${restaurantId}`)
//         .then(response => response.json())
//         .then(restaurant => {
//             let html = `
//                 <div class="col-12 mb-3">
//                     <button class="btn btn-secondary btn-custom" onclick="loadRestaurants()">⬅️ Back to Restaurants</button>
//                     <h3 class="animate__animated animate__fadeInDown">${restaurant.name} - Menu</h3>
//                 </div>
//                 <div class="row">
//             `;
//             restaurant.menuItems.forEach(item => {
//                 html += `
//                     <div class="col-md-6 mb-4">
//                         <div class="card menu-card animate__animated animate__fadeIn">
//                             <img src="${item.imageUrl}" class="card-img-top" alt="${item.name}">
//                             <div class="card-body">
//                                 <h5 class="card-title">${item.name}</h5>
//                                 <p class="card-text">${item.description}</p>
//                                 <p class="card-text"><strong>$${item.price.toFixed(2)}</strong></p>
//                                 <button class="btn btn-success btn-custom" onclick="addToCart(${restaurant.id}, ${item.id})">Add to Cart</button>
//                             </div>
//                         </div>
//                     </div>
//                 `;
//             });
//             html += `</div>`;
//             document.getElementById("restaurantList").innerHTML = html;
//         })
//         .catch(error => console.error("Error loading menu:", error));
// }
//
// // ------------------------------
// // Cart Functions
// // ------------------------------
// function loadCart() {
//     fetch(`${API_BASE}/cart/customer/${getCustomerId()}`)
//         .then(response => response.json())
//         .then(cartItems => {
//             let html = "";
//             if (cartItems.length === 0) {
//                 html = "<p class='text-center lead'>Your cart is empty.</p>";
//             } else {
//                 cartItems.forEach(item => {
//                     html += `
//                         <div class="card cart-card mb-2 p-2 animate__animated animate__fadeIn">
//                             <div class="d-flex justify-content-between align-items-center">
//                                 <span>${item.name} (x${item.quantity})</span>
//                                 <span>$${(item.price * item.quantity).toFixed(2)}</span>
//                                 <button class="btn btn-sm btn-danger btn-custom" onclick="removeFromCart(${item.id})">Remove</button>
//                             </div>
//                         </div>
//                     `;
//                 });
//             }
//             document.getElementById("cartList").innerHTML = html;
//         })
//         .catch(error => console.error("Error loading cart:", error));
// }
//
// function addToCart(restaurantId, itemId) {
//     fetch(`${API_BASE}/cart/add`, {
//         method: "POST",
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify({ restaurantId, itemId, quantity: 1, customerId: getCustomerId() })
//     })
//         .then(() => {
//             alert("Item added to cart!");
//             loadCart();
//         })
//         .catch(error => console.error("Error adding to cart:", error));
// }
//
// function removeFromCart(itemId) {
//     fetch(`${API_BASE}/cart/remove/${itemId}`, { method: "DELETE" })
//         .then(() => loadCart())
//         .catch(error => console.error("Error removing from cart:", error));
// }
//
// // ------------------------------
// // Checkout and Order Functions
// // ------------------------------
// function checkout() {
//     fetch(`{API_BASE}/orders/place?customerId=${getCustomerId()}`, {
//         method: "POST"
//     })
//         .then(() => {
//             alert("Order placed successfully!");
//             loadCart();
//             loadOrderHistory();
//             updateWalletBalance();
//         })
//         .catch(error => console.error("Error during checkout:", error));
// }
//
// function loadOrderHistory() {
//     fetch(`${API_BASE}/orders/customer/${getCustomerId()}`)
//         .then(response => response.json())
//         .then(orders => {
//             let html = "";
//             if (orders.length === 0) {
//                 html = "<p class='text-center lead'>You have not placed any orders yet.</p>";
//             } else {
//                 orders.forEach(order => {
//                     html += `
//                         <div class="card order-card mb-3 p-3 animate__animated animate__fadeIn">
//                             <div class="d-flex justify-content-between">
//                                 <div>
//                                     <strong>Order #${order.id}</strong><br>
//                                     <small>${order.orderDate}</small>
//                                 </div>
//                                 <span class="badge badge-info">${order.status}</span>
//                             </div>
//                             <p>Total: $${order.total.toFixed(2)}</p>
//                             <button class="btn btn-sm btn-outline-primary btn-custom" onclick="viewOrderDetails(${order.id})">View Details</button>
//                         </div>
//                     `;
//                 });
//             }
//             document.getElementById("orderHistory").innerHTML = html;
//         })
//         .catch(error => console.error("Error loading order history:", error));
// }
//
// function viewOrderDetails(orderId) {
//     fetch(`${API_BASE}/orders/${orderId}`)
//         .then(response => response.json())
//         .then(order => {
//             let html = `<ul class="list-group">`;
//             order.items.forEach(item => {
//                 html += `<li class="list-group-item">${item.name} - $${item.price.toFixed(2)} x ${item.quantity}</li>`;
//             });
//             html += `</ul><p class="mt-2"><strong>Total:</strong> $${order.total.toFixed(2)}</p>`;
//             document.getElementById("orderDetailsContent").innerHTML = html;
//             $("#orderDetailsModal").modal("show");
//         })
//         .catch(error => console.error("Error viewing order details:", error));
// }

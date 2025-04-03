/* global Swal */
import { apiGet } from "./api.js";

document.addEventListener("DOMContentLoaded", async () => {
    // Load user info from localStorage
    const user = JSON.parse(localStorage.getItem("user"));
    if (user) {
        document.getElementById("customerName").textContent = user.email;
        document.getElementById("walletDisplay").textContent = `Wallet: ${user.walletBalance} pts`;
    } else {
        window.location.href = "index.html"; // Redirect to login if not authenticated
        return;
    }

    // Load restaurants if container exists
    const restaurantList = document.getElementById("restaurantsContainer");
    if (restaurantList) {
        await loadRestaurants();
    }
});

async function loadRestaurants() {
    try {
        const restaurants = await apiGet('restaurants/all');
        renderRestaurants(restaurants);
    } catch (error) {
        console.error('Error loading restaurants:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Failed to load restaurants.',
        });
    }
}

function renderRestaurants(restaurants) {
    const container = document.getElementById("restaurantsContainer");
    if (!container) return;

    if (restaurants.length === 0) {
        container.innerHTML = '<p>No restaurants available.</p>';
        return;
    }

    container.innerHTML = "";
    restaurants.forEach((restaurant) => {
        const col = document.createElement("div");
        col.className = "col-md-4 mb-3";
        col.innerHTML = `
            <div class="card fade-in-up">
                <img src="${restaurant.imageUrl || 'assets/images/default.jpg'}" class="card-img-top" alt="${restaurant.name}">
                <div class="card-body">
                    <h5 class="card-title">${restaurant.name}</h5>
                    <p class="card-text">📍 ${restaurant.location}</p>
                    <a href="menu.html?restaurantId=${restaurant.id}" class="btn btn-primary">View Menu</a>
                </div>
            </div>
        `;
        container.appendChild(col);
    });
}



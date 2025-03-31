// js/owner.js
/* global Swal */
import { API_BASE } from './api.js';
import { apiGet, apiPost, apiPut, apiDelete } from "./api.js";

// Use jQuery in noConflict mode
jQuery.noConflict();
const $ = jQuery;

// Initialization on DOMContentLoaded
document.addEventListener("DOMContentLoaded", async () => {
    const user = JSON.parse(localStorage.getItem("user"));
    if (!user || user.role !== "OWNER") {
        window.location.href = "index.html";
        return;
    }

    // Load owner-specific data if the corresponding elements exist.
    if (document.getElementById("restaurantsList")) {
        await loadOwnerRestaurants();
    }
    if (document.getElementById("restaurantSelect")) {
        await loadOwnerRestaurantsForSelect();
    }
    if (document.getElementById("ordersContainer")) {
        await loadOwnerOrders();
    }
    if (document.getElementById("totalRestaurants")) {
        await loadOwnerRestaurantsSummary();
    }
    if (document.getElementById("totalOrders")) {
        await loadOwnerOrdersSummary();
    }
});

// --------------------
// Manage Restaurants Functions
// --------------------
async function loadOwnerRestaurants() {
    const user = JSON.parse(localStorage.getItem("user"));
    try {
        const restaurants = await apiGet(`restaurants/owner/${user.id}`);
        renderOwnerRestaurants(restaurants);
    } catch (error) {
        console.error("Error loading restaurants:", error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Failed to load restaurants.'
        });
    }
}

function renderOwnerRestaurants(restaurants) {
    const container = document.getElementById("restaurantsList");
    container.innerHTML = "";
    if (!restaurants || restaurants.length === 0) {
        container.innerHTML = "<p>No restaurants found.</p>";
        return;
    }
    restaurants.forEach(r => {
        const div = document.createElement("div");
        div.className = "col-md-6 mb-4";
        div.innerHTML = `
            <div class="card fade-in-up">
                <img src="${r.imageUrl || 'assets/images/default.jpg'}" class="card-img-top" alt="${r.name}">
                <div class="card-body">
                    <h5 class="card-title">${r.name}</h5>
                    <p class="card-text">${r.location}</p>
                    <div class="btn-group" role="group">
                        <button class="btn btn-sm btn-primary" onclick="editRestaurant(${r.id})">Edit</button>
                        <button class="btn btn-sm btn-danger" onclick="deleteRestaurant(${r.id})">Delete</button>
                        <button class="btn btn-sm btn-info" onclick="viewMenuItems(${r.id})">View Menu</button>
                    </div>
                </div>
            </div>
        `;
        container.appendChild(div);
    });
}

function showRestaurantForm() {
    const form = document.getElementById("restaurantForm");
    if (form) {
        form.reset();
    }
    const restaurantIdField = document.getElementById("restaurantId");
    if (restaurantIdField) {
        restaurantIdField.value = "";
    }
    const modalTitle = document.getElementById("restaurantFormModalLabel");
    if (modalTitle) {
        modalTitle.textContent = "Add Restaurant";
    }
    const restaurantFormModal = $("#restaurantFormModal");
    if (restaurantFormModal.length) {
        restaurantFormModal.modal("show");
    } else {
        console.error("Error: Modal element not found.");
    }
}

function closeRestaurantForm() {
    $("#restaurantFormModal").modal("hide");
}

function viewMenuItems(restaurantId) {
    // Redirect to Manage Menus page with the restaurantId as query parameter.
    window.location.href = `manageMenus.html?restaurantId=${restaurantId}`;
}

// Expose functions globally for inline HTML handlers.
window.showRestaurantForm = showRestaurantForm;
window.closeRestaurantForm = closeRestaurantForm;
window.editRestaurant = editRestaurant;
window.deleteRestaurant = deleteRestaurant;
window.viewMenuItems = viewMenuItems;

document.getElementById("restaurantForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const user = JSON.parse(localStorage.getItem("user"));
    const id = document.getElementById("restaurantId").value;
    const name = document.getElementById("restaurantName").value;
    const location = document.getElementById("restaurantLocation").value;
    const imageUrl = document.getElementById("restaurantImageUrl").value;
    const restaurantData = { name, location, imageUrl, owner: { id: user.id } };

    try {
        if (id) {
            await apiPut(`restaurants/${id}`, restaurantData);
            Swal.fire({
                icon: 'success',
                title: 'Restaurant Updated',
                text: 'Your restaurant has been updated successfully!',
                timer: 1500,
                showConfirmButton: false
            });
        } else {
            await apiPost("restaurants/add", restaurantData);
            Swal.fire({
                icon: 'success',
                title: 'Restaurant Added',
                text: 'Your restaurant has been added successfully!',
                timer: 1500,
                showConfirmButton: false
            });
        }
        closeRestaurantForm();
        await loadOwnerRestaurants();
        if (document.getElementById("restaurantSelect")) {
            await loadOwnerRestaurantsForSelect();
        }
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: "Failed to save restaurant: " + (error.message || "Unknown error")
        });
    }
});

async function editRestaurant(restaurantId) {
    try {
        const restaurant = await apiGet(`restaurants/${restaurantId}`);
        document.getElementById("restaurantId").value = restaurant.id;
        document.getElementById("restaurantName").value = restaurant.name;
        document.getElementById("restaurantLocation").value = restaurant.location;
        document.getElementById("restaurantImageUrl").value = restaurant.imageUrl || "";
        const modalTitle = document.getElementById("restaurantFormModalLabel");
        if (modalTitle) {
            modalTitle.textContent = "Edit Restaurant";
        }
        $("#restaurantFormModal").modal("show");
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: "Failed to load restaurant details: " + (error.message || "Unknown error")
        });
    }
}

async function deleteRestaurant(restaurantId) {
    if (confirm("Are you sure you want to delete this restaurant?")) {
        try {
            const response = await fetch(`${API_BASE}/restaurants/${restaurantId}`, {
                method: 'DELETE'
            });
            // Check for HTTP OK and ignore the body if empty.
            if (!response.ok) {
                throw new Error("Delete request failed");
            }
            // No need to call response.json() here since the body is empty.
            await loadOwnerRestaurants();
            if (document.getElementById("restaurantSelect")) {
                await loadOwnerRestaurantsForSelect();
            }
        } catch (error) {
            alert("Failed to delete restaurant: " + error.message);
        }
    }
}

// --------------------
// Manage Menus Functions
// --------------------
async function loadOwnerRestaurantsForSelect() {
    const user = JSON.parse(localStorage.getItem("user"));
    try {
        const restaurants = await apiGet(`restaurants/owner/${user.id}`);
        const select = document.getElementById("restaurantSelect");
        if (select) {
            select.innerHTML = `<option value="">Select a Restaurant</option>`;
            restaurants.forEach(r => {
                const option = document.createElement("option");
                option.value = r.id;
                option.textContent = r.name;
                select.appendChild(option);
            });
            // Preselect if restaurantId exists in URL query
            const urlParams = new URLSearchParams(window.location.search);
            const selectedRestaurantId = urlParams.get("restaurantId");
            if (selectedRestaurantId) {
                select.value = selectedRestaurantId;
                await loadMenuItems(selectedRestaurantId);
            }
            select.addEventListener("change", () => {
                const restaurantId = select.value;
                if (restaurantId) loadMenuItems(restaurantId);
                else document.getElementById("menuItemsList").innerHTML = "";
            });
        }
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: "Failed to load restaurants for menu management: " + (error.message || "Unknown error")
        });
    }
}

async function loadMenuItems(restaurantId) {
    try {
        const items = await apiGet(`menuitems/restaurant/${restaurantId}`);
        renderMenuItems(items);
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: "Failed to load menu items: " + (error.message || "Unknown error")
        });
    }
}

function renderMenuItems(items) {
    const container = document.getElementById("menuItemsList");
    container.innerHTML = "";
    if (!items || items.length === 0) {
        container.innerHTML = "<p>No menu items found for this restaurant.</p>";
        return;
    }
    items.forEach(item => {
        const div = document.createElement("div");
        div.className = "col-md-4 mb-3";
        div.innerHTML = `
            <div class="card fade-in-up">
                <img src="${item.imageUrl || 'assets/images/default.jpg'}" class="card-img-top" alt="${item.name}">
                <div class="card-body">
                    <h5 class="card-title">${item.name}</h5>
                    <p class="card-text">${item.description || ""}</p>
                    <p class="card-text"><strong>$${item.price.toFixed(2)}</strong></p>
                    <button class="btn btn-sm btn-primary" onclick="editMenuItem(${item.id})">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteMenuItem(${item.id})">Delete</button>
                </div>
            </div>
        `;
        container.appendChild(div);
    });
}

function showMenuItemForm() {
    const form = document.getElementById("menuItemForm");
    if (form) {
        form.reset();
    }
    document.getElementById("menuItemId").value = "";
    document.getElementById("menuItemFormTitle").textContent = "Add Menu Item";
    $("#menuItemFormModal").modal("show");
}

function closeMenuItemForm() {
    $("#menuItemFormModal").modal("hide");
}

document.getElementById("menuItemForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const restaurantId = document.getElementById("restaurantSelect").value;
    if (!restaurantId) {
        Swal.fire({
            icon: 'warning',
            title: 'Select Restaurant',
            text: 'Please select a restaurant.'
        });
        return;
    }
    const id = document.getElementById("menuItemId").value;
    const name = document.getElementById("menuItemName").value;
    const description = document.getElementById("menuItemDescription").value;
    const price = parseFloat(document.getElementById("menuItemPrice").value);
    const imageUrl = document.getElementById("menuItemImageUrl").value;
    const menuItemData = { name, description, price, imageUrl, restaurant: { id: restaurantId } };

    try {
        if (id) {
            await apiPut(`menuitems/${id}`, menuItemData);
            Swal.fire({
                icon: 'success',
                title: 'Updated!',
                text: 'Menu item updated successfully!',
                timer: 1500,
                showConfirmButton: false
            });
        } else {
            await apiPost("menuitems", menuItemData);
            Swal.fire({
                icon: 'success',
                title: 'Added!',
                text: 'Menu item added successfully!',
                timer: 1500,
                showConfirmButton: false
            });
        }
        closeMenuItemForm();
        await loadMenuItems(restaurantId);
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: "Failed to save menu item: " + (error.message || "Unknown error")
        });
    }
});

async function editMenuItem(menuItemId) {
    try {
        const item = await apiGet(`menuitems/${menuItemId}`);
        document.getElementById("menuItemId").value = item.id;
        document.getElementById("menuItemName").value = item.name;
        document.getElementById("menuItemDescription").value = item.description;
        document.getElementById("menuItemPrice").value = item.price;
        document.getElementById("menuItemImageUrl").value = item.imageUrl || "";
        document.getElementById("menuItemFormTitle").textContent = "Edit Menu Item";
        $("#menuItemFormModal").modal("show");
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: "Failed to load menu item details: " + (error.message || "Unknown error")
        });
    }
}

function deleteMenuItem(menuItemId) {
    Swal.fire({
        title: 'Are you sure?',
        text: 'You won\'t be able to revert this!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, delete it!',
        cancelButtonText: 'No, cancel!'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`${API_BASE}/menuitems/${menuItemId}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        Swal.fire('Deleted!', 'Menu item has been deleted.', 'success');
                        location.reload();
                    } else {
                        return response.json().then(err => {
                            throw new Error(err.message || 'Failed to delete menu item');
                        }).catch(() => {
                            throw new Error('Failed to delete menu item.');
                        });
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    Swal.fire('Error!', `Failed to delete menu item: ${error.message}`, 'error');
                });
        } else {
            Swal.fire('Cancelled', 'The menu item is safe.', 'info');
        }
    });
}

// --------------------
// Owner Orders Functions
// --------------------
async function loadOwnerOrders() {
    const user = JSON.parse(localStorage.getItem("user"));
    try {
        const orders = await apiGet(`orders/owner/${user.id}`);
        renderOwnerOrders(orders);
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: "Failed to load orders: " + (error.message || "Unknown error")
        });
    }
}

function renderOwnerOrders(orders) {
    const container = document.getElementById("ordersContainer");
    container.innerHTML = "";
    if (!orders || orders.length === 0) {
        container.innerHTML = "<p>No orders found.</p>";
        return;
    }
    orders.forEach(order => {
        const div = document.createElement("div");
        div.className = "card mb-3";
        let detailsHtml = "";
        if (order.orderDetails && order.orderDetails.length > 0) {
            detailsHtml = '<ul class="list-group list-group-flush">';
            order.orderDetails.forEach(detail => {
                detailsHtml += `<li class="list-group-item">
                                  ${detail.menuItem.name} - $${detail.price.toFixed(2)} (x${detail.quantity})
                                </li>`;
            });
            detailsHtml += '</ul>';
        }
        div.innerHTML = `
            <div class="card-body">
              <h5 class="card-title">Order #${order.id}</h5>
              <p class="card-text">Total: $${order.total.toFixed(2)}</p>
              <p class="card-text">Status: ${order.status}</p>
              <p class="card-text">Order Date: ${new Date(order.orderDate).toLocaleString()}</p>
              ${detailsHtml}
            </div>
        `;
        container.appendChild(div);
    });
}

async function loadOwnerRestaurantsSummary() {
    const user = JSON.parse(localStorage.getItem("user"));
    try {
        const restaurants = await apiGet(`restaurants/owner/${user.id}`);
        document.getElementById("totalRestaurants").textContent = restaurants.length;
    } catch (error) {
        console.error("Error loading restaurants summary:", error);
    }
}

async function loadOwnerOrdersSummary() {
    const user = JSON.parse(localStorage.getItem("user"));
    try {
        const orders = await apiGet(`orders/owner/${user.id}`);
        document.getElementById("totalOrders").textContent = orders.length;
    } catch (error) {
        console.error("Error loading orders summary:", error);
    }
}

// Expose functions globally for inline usage.
window.showRestaurantForm = showRestaurantForm;
window.closeRestaurantForm = closeRestaurantForm;
window.editRestaurant = editRestaurant;
window.deleteRestaurant = deleteRestaurant;
window.viewMenuItems = viewMenuItems;
window.editMenuItem = editMenuItem;
window.deleteMenuItem = deleteMenuItem;
window.showMenuItemForm = showMenuItemForm;

export { loadOwnerRestaurants, loadOwnerOrders, showRestaurantForm, closeRestaurantForm };

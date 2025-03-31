// js/auth.js
import { apiPost } from "./api.js";

document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");

    loginForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const email = document.getElementById("loginEmail").value;
        const password = document.getElementById("loginPassword").value;
        try {
            const data = await apiPost("auth/login", { email, password });
            localStorage.setItem("user", JSON.stringify(data));
            if (data.role === "CUSTOMER") {
                window.location.href = "customer-dashboard.html";
            } else if (data.role === "OWNER") {
                window.location.href = "owner-dashboard.html";
            }
        } catch (error) {
            alert("Login failed: " + error.message);
        }
    });

    registerForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const email = document.getElementById("registerEmail").value;
        const password = document.getElementById("registerPassword").value;
        const role = document.getElementById("registerRole").value;
        try {
            const data = await apiPost("auth/register", { email, password, role });
            localStorage.setItem("user", JSON.stringify(data));
            if (data.role === "CUSTOMER") {
                window.location.href = "customer-dashboard.html";
            } else if (data.role === "OWNER") {
                window.location.href = "owner-dashboard.html";
            }
        } catch (error) {
            alert("Registration failed: " + error.message);
        }
    });
});

// js/auth.js
/* global Swal */
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
                Swal.fire({
                    icon: 'success',
                    title: 'Login Successful',
                    text: 'Your account has been created!',
                    timer: 1500,
                    showConfirmButton: false
                });
                window.location.href = "customer-dashboard.html";
            } else if (data.role === "OWNER") {
                Swal.fire({
                    icon: 'success',
                    title: 'Login Successful',
                    text: 'Your account has been created!',
                    timer: 1500,
                    showConfirmButton: false
                });
                window.location.href = "owner-dashboard.html";
            }
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: 'Login Error',
                text: error.message || 'Login failed. Please try again.'
            });

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
                Swal.fire({
                    icon: 'success',
                    title: 'Registration Successful',
                    text: 'Your account has been created!',
                    timer: 1500,
                    showConfirmButton: false
                });
                window.location.href = "customer-dashboard.html";
            } else if (data.role === "OWNER") {
                Swal.fire({
                    icon: 'success',
                    title: 'Registration Successful',
                    text: 'Your account has been created!',
                    timer: 1500,
                    showConfirmButton: false
                });
                window.location.href = "owner-dashboard.html";
            }
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: 'Registration Error',
                text: error.message || 'Registration failed. Please try again.'
            });

        }
    });
});

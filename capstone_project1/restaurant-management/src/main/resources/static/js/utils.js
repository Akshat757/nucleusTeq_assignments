// js/utils.js

export function showAlert(message, type = "info") {
    // For simplicity using alert(); replace with Toastify/SweetAlert as needed.
    alert(`${type.toUpperCase()}: ${message}`);
}

export function formatCurrency(amount) {
    return `$${amount.toFixed(2)}`;
}

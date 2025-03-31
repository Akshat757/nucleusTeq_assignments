// js/api.js
export const API_BASE = "http://localhost:8081/api";

async function handleResponse(response) {
    if (!response.ok) {
        let errorData;
        try {
            errorData = await response.json();
        } catch {
            errorData = { error: await response.text() };
        }
        throw new Error(errorData.error || `Request failed with status ${response.status}`);
    }
    return response.json();
}

export async function apiGet(endpoint) {
    const response = await fetch(`${API_BASE}/${endpoint}`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem('token')}`
        }
    });
    return handleResponse(response);
}

export async function apiPost(endpoint, data) {
    const response = await fetch(`${API_BASE}/${endpoint}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(data)
    });
    return handleResponse(response);
}

export async function apiPut(endpoint, data) {
    const response = await fetch(`${API_BASE}/${endpoint}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(data)
    });
    return handleResponse(response);
}

export async function apiDelete(endpoint) {
    const response = await fetch(`${API_BASE}/${endpoint}`, {
        method: 'DELETE'
    });
    // If no content (or 204 No Content), return an empty object.
    if (response.status === 204 || !response.headers.get('Content-Length')) {
        return {};
    }
    return response.json();
}

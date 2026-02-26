function apiGet(url) {
    return apiRequest(url, {
        method: 'GET'
    });
}

function apiPost(url, data) {
    return apiRequest(url, {
        method: 'POST',
        body: JSON.stringify(data)
    });
}

function apiPut(url, data) {
    return apiRequest(url, {
        method: 'PUT',
        body: JSON.stringify(data)
    });
}

function apiDelete(url) {
    return apiRequest(url, {
        method: 'DELETE'
    });
}

async function apiRequest(url, options = {}) {
    try {
        const defaultHeaders = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        };

        const response = await fetch(url, {
            ...options,
            headers: {
                ...defaultHeaders,
                ...(options.headers || {})
            }
        });

        // Auto logout on token expiry
        if (response.status === 401) {
            clearToken();
            clearUserDetails();
            clearUserId();
            window.location.href = '/login.html';
            return; // Stop further execution
        }

        // Handle successful responses
        if (response.ok) {
            // If status is 204 No Content, there is no body to parse
            if (response.status === 204) {
                return null;
            }
            // Otherwise, parse the JSON body
            const text = await response.text();
            return text ? JSON.parse(text) : null;
        }

        // Handle error responses
        let errorMessage = `Request failed with status: ${response.status}`;
        try {
            const errorData = await response.json();
            errorMessage = errorData.validationErrors ? errorData.validationErrors.join(', ') : (errorData.message || errorMessage);
        } catch {
            // Ignore if error response is not JSON
        }

        throw new Error(errorMessage);

    } catch (error) {
        console.error('API Error:', error);
        if (typeof showToast === 'function') {
            showToast(error.message || 'An unexpected error occurred.', 'danger');
        }
        throw error;
    }
}

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

const CONTEXT_PATH = "/fitness-app";

async function apiRequest(url, options = {}) {
    const finalUrl = url.startsWith('http') ? url : CONTEXT_PATH + url;
    try {
        const defaultHeaders = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        };

        const response = await fetch(finalUrl, {
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
            window.location.href = '../page/login.html';
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
            if (errorData.validationErrors && typeof errorData.validationErrors === 'object') {
                // Format validation errors from object to string
                errorMessage = Object.entries(errorData.validationErrors)
                    .map(([field, msg]) => `${field}: ${msg}`)
                    .join(';');
            } else {
                errorMessage = errorData.message || errorMessage;
            }
        } catch (jsonError) {
            // If response is not JSON or parsing fails, use generic message
            console.error("Failed to parse error response JSON:", jsonError);
        }

        throw new Error(errorMessage);

    } catch (error) {
        console.error('API Error:', error);
        // REMOVED: showToast call from here. The calling function will handle it.
        throw error;
    }
}

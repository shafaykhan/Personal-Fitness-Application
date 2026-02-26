const API_BASE_URL = "http://localhost:9000/fitness-app";

document.getElementById("loginForm")?.addEventListener("submit", async (e) => {
      e.preventDefault();

      const usernameInput = document.getElementById("username");
      const passwordInput = document.getElementById("password");
      const errorElement = document.getElementById("error");

      if (!usernameInput || !passwordInput || !errorElement) {
            console.error("Required elements not found");
            return;
      }

      const username = usernameInput.value;
      const password = passwordInput.value;

      try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, {
                  method: "POST",
                  headers: {
                        "Content-Type": "application/json"
                  },
                  body: JSON.stringify({username, password})
            });

            if (response.ok) {
                  const data = await response.json();
                  if (data.token) {
                        saveToken(data.token);
                        saveUserDetails(data.userDTO);

                        window.location.href = "../page/dashboard.html";
                  } else {
                        errorElement.innerText = "Login successful but no token received.";
                  }
            } else {
                  const errorData = await response.json().catch(() => ({}));
                  errorElement.innerText = errorData.message || "Login failed. Please check your credentials.";
            }
      } catch (error) {
            console.error("Login error:", error);
            errorElement.innerText = "An error occurred. Please try again later.";
      }
});

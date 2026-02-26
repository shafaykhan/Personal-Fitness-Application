function saveToken(token) {
      localStorage.setItem("auth_token", token);
}

function getToken() {
      return localStorage.getItem("auth_token");
}

function clearToken() {
      localStorage.removeItem("auth_token");
}

function saveUserDetails(userDetails) {
      localStorage.setItem("user_details", JSON.stringify(userDetails));
      if (userDetails.id) {
            localStorage.setItem("userId", userDetails.id);
      }
}

function getUserDetails() {
      const user = localStorage.getItem("user_details");
      if (!user) return null;

      return JSON.parse(user);
}

function getUserId() {
      const userId = localStorage.getItem("userId");
      if (!userId) return null;

      return userId;
}

function clearUserDetails() {
      localStorage.removeItem("user_details");
}

function clearUserId() {
      localStorage.removeItem("userId");
}

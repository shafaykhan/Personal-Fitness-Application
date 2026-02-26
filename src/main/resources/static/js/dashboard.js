if (!getToken()) {
      window.location.href = "/fitness-app/page/login.html";
}

async function loadDashboard() {
      // Any dashboard-specific loading logic can go here.
      // Username population is now handled in common.js
}

// Listen for the custom event dispatched by common.js
document.addEventListener('componentsLoaded', loadDashboard);

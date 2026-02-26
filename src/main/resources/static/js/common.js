async function loadComponent(elementId, filePath) {
      try {
            const response = await fetch(filePath);
            if (response.ok) {
                  const content = await response.text();
                  const element = document.getElementById(elementId);
                  if (element) {
                        element.outerHTML = content;
                  }
            } else {
                  console.error(`Failed to load ${filePath}: ${response.statusText}`);
            }
      } catch (error) {
            console.error(`Error loading ${filePath}:`, error);
      }
}

function populateUserDropdown() {
      const user = getUserDetails();
      if (!user) return;

      document.querySelectorAll('.login-user-name')
          .forEach(el => el.textContent = user.name || 'User');
   //   document.querySelector('.login-user-name').textContent = user.name || 'User';
      document.getElementById('login-user-username').textContent = `@${user.username || ''}`;
      document.getElementById('login-user-contact').textContent = user.contactNo || '-';
      const emailEl = document.getElementById('login-user-email');
      if (emailEl && user?.email) {
            emailEl.textContent = user.email || '-';
            emailEl.title = user.email;
      }

}

async function initializeApp() {
      await Promise.all([
            loadComponent("header-container", "/fitness-app/page/component/header.html"),
            loadComponent("sidebar-container", "/fitness-app/page/component/sidebar.html"),
            loadComponent("toast-container-placeholder", "/fitness-app/page/component/toast.html")
      ]);


      populateUserDropdown();

      const script = document.createElement('script');
      script.src = "/fitness-app/js/adminlte.js";
      document.body.appendChild(script);

      const toastObserver = new MutationObserver((mutations, observer) => {
            if (document.getElementById('liveToast')) {
                  document.dispatchEvent(new Event('componentsLoaded'));
                  observer.disconnect();
            }
      });

      toastObserver.observe(document.body, {
            childList: true,
            subtree: true
      });

      document.body.style.opacity = "1";

      // Add event listener for profile button after components are loaded
      const profileButton = document.getElementById('profile-button');
      if (profileButton) {
            profileButton.addEventListener('click', (e) => {
                  e.preventDefault();
                  const userDetails = getUserDetails();
                  if (userDetails && userDetails.id) {
                        sessionStorage.setItem('viewUserId', userDetails.id);
                        window.location.href = '/fitness-app/page/user.html';
                  }
            });
      }

      // Add event listener for logout button
      const logoutButton = document.getElementById('logout-button');
      if (logoutButton) {
            logoutButton.addEventListener('click', (e) => {
                  e.preventDefault();
                  logout();
            });
      }
}

function showToast(message, type = 'success') {

      const toastElement = document.getElementById('liveToast');
      const toastBody = document.getElementById('toast-body');
      const toastTitle = document.getElementById('toast-title');

      if (!toastElement || !toastBody || !toastTitle) {
            console.error('Toast elements not found.');
            return;
      }

      // Validate type
      const allowedTypes = ['success', 'danger', 'warning', 'info'];
      if (!allowedTypes.includes(type)) {
            type = 'info';
      }

      // Remove old bg classes
      toastElement.classList.remove(
          'text-bg-success',
          'text-bg-danger',
          'text-bg-warning',
          'text-bg-info'
      );

      // Set content
      toastBody.textContent = message;
      toastTitle.textContent =
          type.charAt(0).toUpperCase() + type.slice(1);

      // Add new class
      toastElement.classList.add(`text-bg-${type}`);

      // Show toast
      const toast = new bootstrap.Toast(toastElement, {delay: 3000, autohide: true});
      toast.show();
}

async function logout() {
      const userId = getUserId();
      if (userId) {
            try {
                  await apiPost(`/auth/logout/${userId}`);
            } catch (error) {
                  console.error("Logout failed:", error);
            }
      }

      clearToken();
      clearUserDetails();
      clearUserId();
      window.location.href = "/fitness-app/page/login.html";
}

document.addEventListener("DOMContentLoaded", initializeApp);

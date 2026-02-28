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

function applySidebarAccessControl() {
      const userDetails = getUserDetails();
      const isAdmin = userDetails && userDetails.role === 'ADMIN';

      const identityHeader = document.getElementById('sidebar-header-identity');
      const userItem = document.getElementById('sidebar-item-user');
      const lookupItem = document.getElementById('sidebar-item-lookup');

      if (identityHeader) {
            identityHeader.style.display = isAdmin ? '' : 'none';
      }
      if (userItem) {
            userItem.style.display = isAdmin ? '' : 'none';
      }
      if (lookupItem) {
            lookupItem.style.display = isAdmin ? '' : 'none';
      }
}

function populateUserDropdown() {
      const user = getUserDetails();
      if (!user) return;

      document.querySelectorAll('.login-user-name')
          .forEach(el => el.textContent = user.name || 'User');
      document.getElementById('login-user-username').textContent = `@${user.username || ''}`;
      document.getElementById('login-user-contact').textContent = user.contactNo || '-';
      const emailEl = document.getElementById('login-user-email');
      if (emailEl && user?.email) {
            emailEl.textContent = user.email || '-';
            emailEl.title = user.email;
      }

}

function setUserProfile() {
      const user = getUserDetails();
      if (!user) return;

      let imageSrc = "/fitness-app/img/man.png";

      if (user.gender === "FEMALE") {
            imageSrc = "/fitness-app/img/woman.png";
      } else if (user.gender === "MALE") {
            imageSrc = "/fitness-app/img/man.png";
      }

      document.querySelectorAll('.user-image')
          .forEach(el => el.src = imageSrc);
}

async function initializeApp() {
      // Check if we are on the login page
      if (window.location.pathname.includes('login.html')) {
            await loadComponent("toast-container-placeholder", "/fitness-app/page/component/toast.html");
            
            // Initialize toast observer for login page
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
            return;
      }

      await Promise.all([
            loadComponent("header-container", "/fitness-app/page/component/header.html"),
            loadComponent("sidebar-container", "/fitness-app/page/component/sidebar.html"),
            loadComponent("toast-container-placeholder", "/fitness-app/page/component/toast.html")
      ]);

      // Apply sidebar access control after sidebar is loaded
      applySidebarAccessControl();
      populateUserDropdown();
      setUserProfile();

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

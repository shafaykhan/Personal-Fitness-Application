const API_BASE_URL = "/fitness-app";

const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");
const loginButton = document.getElementById("loginButton");
const togglePassword = document.getElementById("togglePassword");
const toggleIcon = document.getElementById("toggleIcon");

function validateInputs() {
    if (usernameInput && passwordInput && loginButton) {
        const username = usernameInput.value.trim();
        const password = passwordInput.value.trim();
        loginButton.disabled = !(username && password);
    }
}

if (usernameInput) {
    usernameInput.addEventListener("input", validateInputs);
}

if (passwordInput) {
    passwordInput.addEventListener("input", validateInputs);
}

if (togglePassword && passwordInput && toggleIcon) {
    togglePassword.addEventListener("click", function () {
        const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
        passwordInput.setAttribute("type", type);
        
        if (type === "password") {
            toggleIcon.classList.remove("bi-eye");
            toggleIcon.classList.add("bi-eye-slash");
        } else {
            toggleIcon.classList.remove("bi-eye-slash");
            toggleIcon.classList.add("bi-eye");
        }
    });
}

document.getElementById("loginForm")?.addEventListener("submit", async (e) => {
      e.preventDefault();

      if (!usernameInput || !passwordInput) {
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
                        showToast("Login successful!", 'success');
                        saveToken(data.token);
                        saveUserDetails(data.userDTO);

                        window.location.href = "../page/dashboard.html";
                  } else {
                        showToast("Login successful but no token received!", 'danger');
                  }
            } else {
                  const errorData = await response.json().catch(() => ({}));
                  showToast(errorData.message || "Login failed. Please check your credentials!", "danger");
            }
      } catch (error) {
            showToast("An error occurred. Please try again later!", 'danger');
      }
});

// Registration Logic
const registerForm = document.getElementById('registerForm');
const registerBtn = document.getElementById('registerBtn');

function validateRegisterForm() {
    const name = document.getElementById('regName').value.trim();
    const username = document.getElementById('regUsername').value.trim();
    const password = document.getElementById('regPassword').value.trim();
    const email = document.getElementById('regEmail').value.trim();
    const contactNo = document.getElementById('regContactNo').value.trim();
    const dateOfBirth = document.getElementById('regDateOfBirth').value;
    const gender = document.getElementById('regGender').value;

    const isValid = name && username && password && email && contactNo && dateOfBirth && gender;
    if (registerBtn) {
        registerBtn.disabled = !isValid;
    }
}

if (registerForm) {
    const inputs = registerForm.querySelectorAll('input, select');
    inputs.forEach(input => {
        input.addEventListener('input', validateRegisterForm);
        input.addEventListener('change', validateRegisterForm);
    });
}

registerForm?.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const user = {
        name: document.getElementById('regName').value,
        username: document.getElementById('regUsername').value,
        password: document.getElementById('regPassword').value,
        email: document.getElementById('regEmail').value,
        contactNo: document.getElementById('regContactNo').value,
        dateOfBirth: document.getElementById('regDateOfBirth').value,
        gender: document.getElementById('regGender').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/api/users/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        });

        if (response.ok) {
            showToast('Welcome! Your account has been created', 'success');
            const modal = bootstrap.Modal.getInstance(document.getElementById('registerModal'));
            modal.hide();
            registerForm.reset();
            validateRegisterForm();
        } else {
            const errorData = await response.json().catch(() => ({}));
      showToast(errorData.message || 'Registration failed. Please try again!', 'danger');
        }
    } catch (error) {
        console.error('Registration error:', error);
        showToast('An error occurred during Registration. Please try again!', 'danger');
    }
});

if (!getToken()) {
    window.location.href = "/fitness-app/page/login.html";
}

let allUsers = [];
let currentPage = 1;
const rowsPerPage = 10;
const loggedInUser = getUserDetails();
const isAdmin = loggedInUser && loggedInUser.role === 'ADMIN';

async function loadUsers() {
    if (!isAdmin) {
        document.getElementById('user-list-card').style.display = 'none';
        document.getElementById('unauthorized-message-card').style.display = 'block';
        return;
    }

    try {
        allUsers = await apiGet('/fitness-app/api/users');
        renderTable(allUsers, currentPage);
        setupPagination(allUsers);
        setupSearch();
    } catch (error) {
        console.error("Error loading users:", error);
        showToast("Error loading users!", "danger");
    }
}

async function loadLookups() {
    try {
        const data = await apiGet('/fitness-app/api/lookups/by-status-active?groupKeys=HEIGHT_UOM~WEIGHT_UOM');
        if (!data) return;
        populateSelect('heightUomId', data.HEIGHT_UOM || []);
        populateSelect('weightUomId', data.WEIGHT_UOM || []);
    } catch (error) {
        console.error("Error loading lookups:", error);
    }
}

async function fetchUser(id) {
    try {
        return await apiGet(`/fitness-app/api/users/${id}`);
    } catch (error) {
        console.error(`Error fetching user ${id}:`, error);
        return null;
    }
}

async function editUser(id) {
    const user = await fetchUser(id);
    if (user) {
        populateForm(user, false);
        showForm();
    }
}

async function viewUser(id) {
    const user = await fetchUser(id);
    if (user) {
        populateForm(user, true);
        showForm();
    }
}

function saveUser(user) {
    return apiPost('/fitness-app/api/users/register', user);
}

function updateUser(user) {
    return apiPut('/fitness-app/api/users', user);
}

function populateSelect(elementId, items) {
    const select = document.getElementById(elementId);
    const currentValue = select.value;
    select.innerHTML = '<option value="">Select UOM</option>';
    items.forEach(item => {
        const option = document.createElement('option');
        option.value = item.id;
        option.textContent = item.value;
        select.appendChild(option);
    });
    if (currentValue) {
        select.value = currentValue;
    }
}

function renderTable(users, page) {
    const tableBody = document.getElementById('user-table-body');
    tableBody.innerHTML = '';

    if (!users || users.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="7" class="text-center">No users found</td></tr>';
        document.getElementById('page-info').innerText = '0 of 0';
        return;
    }

    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    const paginatedUsers = users.slice(start, end);

    paginatedUsers.forEach(user => {
        const row = document.createElement('tr');
        row.classList.add('align-left');
        const statusBadge = user.status === 'ACTIVE' ? '<span class="badge text-bg-success">ACTIVE</span>' : `<span class="badge text-bg-danger">${user.status}</span>`;
        row.innerHTML = `
            <td>${user.name}</td>
            <td>${user.username}</td>
            <td>${user.contactNo}</td>
            <td>${user.email}</td>
            <td>${user.role}</td>
            <td>${statusBadge}</td>
            <td>
                <div class="d-flex justify-content-center gap-2">
                    <button class="btn btn-sm btn-light border text-primary rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="editUser(${user.id})"
                            title="Edit">
                            
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-light border text-secondary rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="viewUser(${user.id})"
                            title="View">
                        <i class="bi bi-eye"></i>
                    </button>
                </div>
            </td>
        `;
        tableBody.appendChild(row);
    });

    const totalUsers = users.length;
    const startRange = start + 1;
    const endRange = Math.min(end, totalUsers);
    document.getElementById('page-info').innerText = `${startRange}-${endRange} of ${totalUsers}`;
}

function setupPagination(users) {
    const paginationElement = document.getElementById('pagination');
    paginationElement.innerHTML = '';
    if (!users || users.length === 0) return;

    const pageCount = Math.ceil(users.length / rowsPerPage);
    for (let i = 1; i <= pageCount; i++) {
        const li = document.createElement('li');
        li.className = `page-item ${i === currentPage ? 'active' : ''}`;
        li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        li.addEventListener('click', (e) => {
            e.preventDefault();
            currentPage = i;
            renderTable(users, currentPage);
            // Update active state
            document.querySelectorAll('.page-item').forEach(item => item.classList.remove('active'));
            li.classList.add('active');
        });
        paginationElement.appendChild(li);
    }
}

function setupSearch() {
    const searchInput = document.getElementById('table-search');
    searchInput.addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase();
        const filteredUsers = allUsers.filter(user =>
            Object.values(user).some(value => String(value).toLowerCase().includes(searchTerm))
        );
        currentPage = 1;
        renderTable(filteredUsers, currentPage);
        setupPagination(filteredUsers);
    });
}

function populateForm(user, isReadOnly) {
    document.getElementById('userId').value = user.id;
    document.getElementById('name').value = user.name;
    document.getElementById('username').value = user.username;
    document.getElementById('password').value = ''; // Clear password for security
    document.getElementById('contactNo').value = user.contactNo;
    document.getElementById('email').value = user.email;
    document.getElementById('dateOfBirth').value = user.dateOfBirth;
    document.getElementById('gender').value = user.gender;
    document.getElementById('height').value = user.height || '';
    document.getElementById('weight').value = user.weight || '';
    document.getElementById('role').value = user.role;
    document.getElementById('status').value = user.status;

    // Ensure UOM options exist and set values
    ensureOption('heightUomId', user.heightUomId, user.heightUomValue);
    document.getElementById('heightUomId').value = user.heightUomId || '';
    
    ensureOption('weightUomId', user.weightUomId, user.weightUomValue);
    document.getElementById('weightUomId').value = user.weightUomId || '';

    // Hide role field for non-admin users
    const formGroupRole = document.getElementById('form-group-role');
    if (formGroupRole) {
        formGroupRole.style.display = isAdmin ? '' : 'none';
    }

    const formElements = document.getElementById('user-form').elements;
    for (let i = 0; i < formElements.length; i++) {
        formElements[i].disabled = isReadOnly;
    }
    
    document.querySelectorAll('#user-form button[type="button"]').forEach(btn => btn.disabled = false);
    
    const saveBtn = document.getElementById('save-btn');
    saveBtn.style.display = isReadOnly ? 'none' : 'block';
    if (!isReadOnly) {
        saveBtn.disabled = false;
        validateUserForm(); // Validate initially when editing
    }
    
    document.getElementById('form-title').innerText = isReadOnly ? 'View User' : 'Edit User';
}

// Helper function to ensure an option exists in a dropdown
function ensureOption(selectId, value, text) {
      const select = document.getElementById(selectId);
      if (value && !select.querySelector(`option[value='${value}']`)) {
            const option = document.createElement('option');
            option.value = value;
            option.textContent = text || `ID: ${value}`;
            option.disabled = true; // Disable it as it's not an active choice
            select.appendChild(option);
      }
}

function showList() {
    document.getElementById('user-list-card').style.display = 'block';
    document.getElementById('user-form-card').style.display = 'none';
    document.getElementById('unauthorized-message-card').style.display = 'none'; // Hide unauthorized message
}

function showForm(isAdd = false) {
    document.getElementById('user-list-card').style.display = 'none';
    document.getElementById('user-form-card').style.display = 'block';
    document.getElementById('unauthorized-message-card').style.display = 'none'; // Hide unauthorized message

    if (isAdd) {
        document.getElementById('user-form').reset();
        document.getElementById('userId').value = '';
        document.getElementById('form-title').innerText = 'Add User';
        const formElements = document.getElementById('user-form').elements;
        for (let i = 0; i < formElements.length; i++) {
            formElements[i].disabled = false;
        }
        document.getElementById('save-btn').style.display = 'block';
        validateUserForm(); // Validate initially when adding
    }
}

function validateUserForm() {
    const name = document.getElementById('name').value.trim();
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    const contactNo = document.getElementById('contactNo').value.trim();
    const email = document.getElementById('email').value.trim();
    const dateOfBirth = document.getElementById('dateOfBirth').value;
    const gender = document.getElementById('gender').value;
    
    // Check if editing (userId exists) to make password optional if not changed
    const userId = document.getElementById('userId').value;
    const isPasswordValid = userId ? true : !!password; // If editing, password can be empty (unchanged)

    const isValid = name && username && isPasswordValid && contactNo && email && dateOfBirth && gender;
    document.getElementById('save-btn').disabled = !isValid;
}

// Add event listeners for validation
const formInputs = ['name', 'username', 'password', 'contactNo', 'email', 'dateOfBirth', 'gender'];
formInputs.forEach(id => {
    const element = document.getElementById(id);
    if (element) {
        element.addEventListener('input', validateUserForm);
        element.addEventListener('change', validateUserForm);
    }
});

document.getElementById('user-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const userId = document.getElementById('userId').value;
    const user = {
        id: userId || null,
        name: document.getElementById('name').value,
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        contactNo: document.getElementById('contactNo').value,
        email: document.getElementById('email').value,
        dateOfBirth: document.getElementById('dateOfBirth').value || null,
        gender: document.getElementById('gender').value,
        height: document.getElementById('height').value ? parseFloat(document.getElementById('height').value) : null,
        weight: document.getElementById('weight').value ? parseFloat(document.getElementById('weight').value) : null,
        role: document.getElementById('role').value,
        status: document.getElementById('status').value,
        heightUomId: document.getElementById('heightUomId').value || null,
        weightUomId: document.getElementById('weightUomId').value || null
    };

    try {
        await (userId ? updateUser(user) : saveUser(user));
        showToast(`User ${userId ? 'updated' : 'added'} successfully!`, 'success');
        
        const loggedInUser = getUserDetails();
        if (loggedInUser && loggedInUser.id.toString() === userId) {
            setTimeout(() => window.location.href = '/fitness-app/page/dashboard.html', 1000);
        } else {
            showList();
            loadUsers();
        }
    } catch (error) {
        console.error('Error saving user:', error);
        showToast('Error saving user!', 'danger');
    }
});

document.addEventListener('componentsLoaded', async () => {
    await loadLookups(); // Ensure lookups are loaded first
    const viewUserId = sessionStorage.getItem('viewUserId');
    if (viewUserId) {
        document.getElementById('user-list-card').style.display = 'none';
        editUser(viewUserId);
        sessionStorage.removeItem('viewUserId');
    } else {
        loadUsers();
    }
});

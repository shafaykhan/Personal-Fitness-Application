if (!getToken()) {
    window.location.href = "/fitness-app/page/login.html";
}

let allGoals = [];
let currentPage = 1;
const rowsPerPage = 10;

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

async function loadGoals() {
    try {
        const user = getUserDetails();
        allGoals = await apiGet(`/fitness-app/api/goals/by-user/${user.id}`);
        renderTable(allGoals, currentPage);
        setupPagination(allGoals);
        setupSearch();
        await loadLookupData(); // Ensure lookups are loaded before populating forms
    } catch (error) {
        console.error("Error loading goals:", error);
    }
}

async function fetchGoal(id) {
    try {
        return await apiGet(`/fitness-app/api/goals/${id}`);
    } catch (error) {
        console.error(`Error fetching goal ${id}:`, error);
        return null;
    }
}

async function editGoal(id) {
    const goal = await fetchGoal(id);
    if (goal) {
        populateForm(goal, false);
        showForm();
    }
}

async function viewGoal(id) {
    const goal = await fetchGoal(id);
    if (goal) {
        populateForm(goal, true);
        showForm();
    }
}

async function deleteGoal(id) {
    if (confirm("Are you sure you want to delete this goal?")) {
        try {
            await apiDelete(`/fitness-app/api/goals/${id}`);
            showToast("Goal deleted successfully!", "success");
            loadGoals();
        } catch (error) {
            console.error(`Error deleting goal ${id}:`, error);
            showToast("Error deleting goal!", "danger");
        }
    }
}

function renderTable(goals, page) {
    const tableBody = document.getElementById('goal-table-body');
    tableBody.innerHTML = '';

    if (!goals || goals.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="9" class="text-center">No goals found</td></tr>';
        document.getElementById('page-info').innerText = '0 of 0';
        return;
    }

    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    const paginatedGoals = goals.slice(start, end);

    paginatedGoals.forEach(goal => {
        const row = document.createElement('tr');
        row.classList.add('align-left');
        const statusBadge = goal.status === 'ACTIVE' ? '<span class="badge text-bg-success">ACTIVE</span>' : `<span class="badge text-bg-danger">${goal.status}</span>`;
        row.innerHTML = `
            <td>${goal.description}</td>
            <td>${goal.typeValue || 'N/A'}</td>
            <td>${goal.targetValue}</td>
            <td>${goal.currentValue}</td>
            <td>${new Date(goal.startDate).toLocaleDateString()}</td>
            <td>${goal.endDate ? new Date(goal.endDate).toLocaleDateString() : 'N/A'}</td>
            <td>${new Date(goal.recordDateTime).toLocaleString()}</td>
            <td>${statusBadge}</td>
            <td>
                <div class="d-flex justify-content-center gap-2">
                    <button class="btn btn-sm btn-light border text-primary rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="editGoal(${goal.id})"
                            title="Edit">
                            
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-light border text-secondary rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="viewGoal(${goal.id})"
                            title="View">
                        <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-light border text-danger rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="deleteGoal(${goal.id})"
                            title="Delete">
                        <i class="bi bi-trash"></i>
                    </button>
                </div>
            </td>
        `;
        tableBody.appendChild(row);
    });

    const totalGoals = goals.length;
    const startRange = start + 1;
    const endRange = Math.min(end, totalGoals);
    document.getElementById('page-info').innerText = `${startRange}-${endRange} of ${totalGoals}`;
}

function setupPagination(goals) {
    const paginationElement = document.getElementById('pagination');
    paginationElement.innerHTML = '';
    if (!goals || goals.length === 0) return;

    const pageCount = Math.ceil(goals.length / rowsPerPage);
    for (let i = 1; i <= pageCount; i++) {
        const li = document.createElement('li');
        li.className = `page-item ${i === currentPage ? 'active' : ''}`;
        li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        li.addEventListener('click', (e) => {
            e.preventDefault();
            currentPage = i;
            renderTable(goals, currentPage);
            // Update active state
            document.querySelectorAll('.page-item').forEach(item => item.classList.remove('active'));
            li.classList.add('active');
        });
        paginationElement.appendChild(li);
    }
}

function setupSearch() {
    const searchInput = document.getElementById('table-search');
    searchInput.addEventListener('input', () => {
        const searchTerm = searchInput.value.toLowerCase();
        const filteredGoals = allGoals.filter(goal =>
            Object.values(goal).some(value =>
                String(value).toLowerCase().includes(searchTerm)
            )
        );
        currentPage = 1;
        renderTable(filteredGoals, currentPage);
        setupPagination(filteredGoals);
    });
}

function populateForm(goal, isReadOnly) {
    document.getElementById('goalId').value = goal.id || '';
    document.getElementById('description').value = goal.description || '';
    
    if (goal.typeId) {
        ensureOption('typeId', goal.typeId, goal.typeValue);
    }
    
    document.getElementById('typeId').value = goal.typeId || '';
    document.getElementById('targetValue').value = goal.targetValue || '';
    document.getElementById('currentValue').value = goal.currentValue || '';
    document.getElementById('startDate').value = goal.startDate ? new Date(goal.startDate).toISOString().split('T')[0] : '';
    document.getElementById('endDate').value = goal.endDate ? new Date(goal.endDate).toISOString().split('T')[0] : '';
    document.getElementById('recordDateTime').value = goal.recordDateTime ? new Date(goal.recordDateTime).toISOString().slice(0, 16) : '';
    document.getElementById('status').value = goal.status || 'ACTIVE';

    const formElements = document.getElementById('goal-form').elements;
    for (let i = 0; i < formElements.length; i++) {
        formElements[i].disabled = isReadOnly;
    }
    
    // Enable cancel button even in read-only mode
    document.querySelectorAll('#goal-form button[type="button"]').forEach(btn => btn.disabled = false);

    const saveBtn = document.getElementById('save-btn');
    saveBtn.style.display = isReadOnly ? 'none' : 'block';
    if (!isReadOnly) {
        saveBtn.disabled = false;
        validateGoalForm(); // Validate initially when editing
    }
    
    document.getElementById('form-title').innerText = isReadOnly ? 'View Goal' : (goal.id ? 'Edit Goal' : 'Add Goal');
}

function showList() {
    document.getElementById('goal-list-card').style.display = 'block';
    document.getElementById('goal-form-card').style.display = 'none';
}

function showForm(isAdd = false) {
    document.getElementById('goal-list-card').style.display = 'none';
    document.getElementById('goal-form-card').style.display = 'block';
    if (isAdd) {
        document.getElementById('goal-form').reset();
        document.getElementById('goalId').value = '';
        document.getElementById('status').value = 'ACTIVE';
        document.getElementById('form-title').innerText = 'Add Goal';
        const formElements = document.getElementById('goal-form').elements;
        for (let i = 0; i < formElements.length; i++) {
            formElements[i].disabled = false;
        }
        document.getElementById('save-btn').style.display = 'block';
        validateGoalForm(); // Validate initially when adding
    }
}

function validateGoalForm() {
    const typeId = document.getElementById('typeId').value;
    const targetValue = document.getElementById('targetValue').value;
    const currentValue = document.getElementById('currentValue').value;
    const startDate = document.getElementById('startDate').value;
    const recordDateTime = document.getElementById('recordDateTime').value;
    const status = document.getElementById('status').value;

    const isValid = typeId && targetValue && currentValue && startDate && recordDateTime && status;
    document.getElementById('save-btn').disabled = !isValid;
}

// Add event listeners for validation
const formInputs = ['typeId', 'targetValue', 'currentValue', 'startDate', 'recordDateTime', 'status'];
formInputs.forEach(id => {
    const element = document.getElementById(id);
    if (element) {
        element.addEventListener('input', validateGoalForm);
        element.addEventListener('change', validateGoalForm);
    }
});

async function loadLookupData() {
    try {
        const data = await apiGet("/fitness-app/api/lookups/by-status-active?groupKeys=GOAL_TYPE");
        if (!data) return;

        populateSelect('typeId', data.GOAL_TYPE || []);
    } catch (error) {
        console.error("Lookup load failed:", error);
    }
}

function populateSelect(selectId, items) {
    const select = document.getElementById(selectId);
    const currentValue = select.value;
    select.innerHTML = '<option value="">-- Select --</option>';
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

document.getElementById('goal-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const user = getUserDetails();
    const goal = {
        id: document.getElementById('goalId').value || null,
        userId: user.id,
        description: document.getElementById('description').value,
        typeId: document.getElementById('typeId').value,
        targetValue: document.getElementById('targetValue').value,
        currentValue: document.getElementById('currentValue').value,
        startDate: document.getElementById('startDate').value,
        endDate: document.getElementById('endDate').value,
        recordDateTime: new Date(document.getElementById('recordDateTime').value).toISOString(),
        status: document.getElementById('status').value,
    };

    try {
        await apiPost('/fitness-app/api/goals', goal);
        showToast('Goal saved successfully!', 'success');
        showList();
        loadGoals();
    } catch (error) {
        console.error('Error saving goal:', error);
        showToast(error.message, 'danger');
    }
});

document.addEventListener('componentsLoaded', loadGoals);

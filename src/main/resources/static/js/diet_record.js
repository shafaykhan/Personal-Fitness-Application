if (!getToken()) {
    window.location.href = "/fitness-app/page/login.html";
}

let allDietRecords = [];
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

async function loadDietRecords() {
    try {
        allDietRecords = await apiGet(`/api/diet-records/by-user/`+ getUserId());
        renderTable(allDietRecords, currentPage);
        setupPagination(allDietRecords);
        setupSearch();
        await loadLookupData(); // Ensure lookups are loaded before populating forms
    } catch (error) {
        console.error("Error loading diet records:", error);
    }
}

async function fetchDietRecord(id) {
    try {
        return await apiGet(`/api/diet-records/${id}`);
    } catch (error) {
        console.error(`Error fetching diet record ${id}:`, error);
        return null;
    }
}

async function editDietRecord(id) {
    const record = await fetchDietRecord(id);
    if (record) {
        populateForm(record, false);
        showForm();
    }
}

async function viewDietRecord(id) {
    const record = await fetchDietRecord(id);
    if (record) {
        populateForm(record, true);
        showForm();
    }
}

async function deleteDietRecord(id) {
    if (confirm("Are you sure you want to delete this record?")) {
        try {
            await apiDelete(`/api/diet-records/${id}`);
            showToast("Record deleted successfully!", "success");
            loadDietRecords();
        } catch (error) {
            console.error(`Error deleting diet record ${id}:`, error);
            showToast("Error deleting record!", "danger");
        }
    }
}

function renderTable(records, page) {
    const tableBody = document.getElementById('diet-record-table-body');
    tableBody.innerHTML = '';

    if (!records || records.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="9" class="text-center">No diet records found</td></tr>';
        document.getElementById('page-info').innerText = '0 of 0';
        return;
    }

    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    const paginatedRecords = records.slice(start, end);

    paginatedRecords.forEach(record => {
        const row = document.createElement('tr');
        row.classList.add('align-left');
        const statusBadge = record.status === 'ACTIVE' ? '<span class="badge text-bg-success">ACTIVE</span>' : `<span class="badge text-bg-danger">${record.status}</span>`;
        row.innerHTML = `
            <td>${record.foodName}</td>
            <td>${record.mealTypeValue || 'N/A'}</td>
            <td>${record.calories}</td>
            <td>${record.protein}</td>
            <td>${record.carbs}</td>
            <td>${record.fat}</td>
            <td>${new Date(record.recordDateTime).toLocaleString()}</td>
            <td>${statusBadge}</td>
            <td>
                <div class="d-flex justify-content-center gap-2">
                    <button class="btn btn-sm btn-light border text-primary rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="editDietRecord(${record.id})"
                            title="Edit">
                            
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-light border text-secondary rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="viewDietRecord(${record.id})"
                            title="View">
                        <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-light border text-danger rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="deleteDietRecord(${record.id})"
                            title="Delete">
                        <i class="bi bi-trash"></i>
                    </button>
                </div>
            </td>
        `;
        tableBody.appendChild(row);
    });

    const totalRecords = records.length;
    const startRange = start + 1;
    const endRange = Math.min(end, totalRecords);
    document.getElementById('page-info').innerText = `${startRange}-${endRange} of ${totalRecords}`;
}

function setupPagination(records) {
    const paginationElement = document.getElementById('pagination');
    paginationElement.innerHTML = '';
    if (!records || records.length === 0) return;

    const pageCount = Math.ceil(records.length / rowsPerPage);
    for (let i = 1; i <= pageCount; i++) {
        const li = document.createElement('li');
        li.className = `page-item ${i === currentPage ? 'active' : ''}`;
        li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        li.addEventListener('click', (e) => {
            e.preventDefault();
            currentPage = i;
            renderTable(records, currentPage);
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
        const filteredRecords = allDietRecords.filter(record =>
            Object.values(record).some(value =>
                String(value).toLowerCase().includes(searchTerm)
            )
        );
        currentPage = 1;
        renderTable(filteredRecords, currentPage);
        setupPagination(filteredRecords);
    });
}

function populateForm(record, isReadOnly) {
    document.getElementById('dietRecordId').value = record.id || '';
    document.getElementById('foodName').value = record.foodName || '';
    
    if (record.mealTypeId) {
        ensureOption('mealTypeId', record.mealTypeId, record.mealTypeValue);
    }
    
    document.getElementById('userId').value = record.userId || getUserId();
    document.getElementById('mealTypeId').value = record.mealTypeId || '';
    document.getElementById('calories').value = record.calories || '';
    document.getElementById('protein').value = record.protein || '';
    document.getElementById('carbs').value = record.carbs || '';
    document.getElementById('fat').value = record.fat || '';
    document.getElementById('recordDateTime').value = record.recordDateTime ? new Date(record.recordDateTime).toISOString().slice(0, 16) : '';
    document.getElementById('status').value = record.status || 'ACTIVE';

    const formElements = document.getElementById('diet-record-form').elements;
    for (let i = 0; i < formElements.length; i++) {
        formElements[i].disabled = isReadOnly;
    }
    
    // Enable cancel button even in read-only mode
    document.querySelectorAll('#diet-record-form button[type="button"]').forEach(btn => btn.disabled = false);

    const saveBtn = document.getElementById('save-btn');
    saveBtn.style.display = isReadOnly ? 'none' : 'block';
    if (!isReadOnly) {
        saveBtn.disabled = false;
        validateDietRecordForm(); // Validate initially when editing
    }
    
    document.getElementById('form-title').innerText = isReadOnly ? 'View Diet Record' : (record.id ? 'Edit Diet Record' : 'Add Diet Record');
}

function showList() {
    document.getElementById('diet-record-list-card').style.display = 'block';
    document.getElementById('diet-record-form-card').style.display = 'none';
}

function showForm(isAdd = false) {
    document.getElementById('diet-record-list-card').style.display = 'none';
    document.getElementById('diet-record-form-card').style.display = 'block';
    if (isAdd) {
        document.getElementById('diet-record-form').reset();
        document.getElementById('dietRecordId').value = '';
        document.getElementById('status').value = 'ACTIVE';
        document.getElementById('form-title').innerText = 'Add Diet Record';
        const formElements = document.getElementById('diet-record-form').elements;
        for (let i = 0; i < formElements.length; i++) {
            formElements[i].disabled = false;
        }
        document.getElementById('save-btn').style.display = 'block';
        validateDietRecordForm(); // Validate initially when adding
    }
}

function validateDietRecordForm() {
    const foodName = document.getElementById('foodName').value.trim();
    const mealTypeId = document.getElementById('mealTypeId').value;
    const recordDateTime = document.getElementById('recordDateTime').value;
    const status = document.getElementById('status').value;

    const isValid = foodName && mealTypeId && recordDateTime && status;
    document.getElementById('save-btn').disabled = !isValid;
}

// Add event listeners for validation
const formInputs = ['foodName', 'mealTypeId', 'recordDateTime', 'status'];
formInputs.forEach(id => {
    const element = document.getElementById(id);
    if (element) {
        element.addEventListener('input', validateDietRecordForm);
        element.addEventListener('change', validateDietRecordForm);
    }
});

async function loadLookupData() {
    try {
        const data = await apiGet("/api/lookups/by-status-active?groupKeys=MEAL_TYPE");
        if (!data) return;

        populateSelect('mealTypeId', data.MEAL_TYPE || []);
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

document.getElementById('diet-record-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const record = {
        id: document.getElementById('dietRecordId').value || null,
        userId: getUserId(),
        foodName: document.getElementById('foodName').value,
        mealTypeId: document.getElementById('mealTypeId').value,
        calories: document.getElementById('calories').value,
        protein: document.getElementById('protein').value,
        carbs: document.getElementById('carbs').value,
        fat: document.getElementById('fat').value,
        recordDateTime: new Date(document.getElementById('recordDateTime').value).toISOString(),
        status: document.getElementById('status').value,
    };

    try {
        await apiPost('/api/diet-records', record);
        showToast('Diet record saved successfully', 'success');
        showList();
        loadDietRecords();
    } catch (error) {
        console.error('Error saving diet record:', error);
        showToast('Error saving diet record!', 'danger');
    }
});

document.addEventListener('componentsLoaded', loadDietRecords);

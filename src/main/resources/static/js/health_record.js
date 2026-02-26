if (!getToken()) {
    window.location.href = "/fitness-app/page/login.html";
}

let allHealthRecords = [];
let currentPage = 1;
const rowsPerPage = 10;

async function loadHealthRecords() {
    try {
        const user = getUserDetails();
        allHealthRecords = await apiGet(`/fitness-app/api/health-records/by-user/${user.id}`);
        renderTable(allHealthRecords, currentPage);
        setupPagination(allHealthRecords);
        setupSearch();
    } catch (error) {
        console.error("Error loading health records:", error);
    }
}

async function fetchHealthRecord(id) {
    try {
        return await apiGet(`/fitness-app/api/health-records/${id}`);
    } catch (error) {
        console.error(`Error fetching health record ${id}:`, error);
        return null;
    }
}

async function editHealthRecord(id) {
    const record = await fetchHealthRecord(id);
    if (record) {
        populateForm(record, false);
        showForm();
    }
}

async function viewHealthRecord(id) {
    const record = await fetchHealthRecord(id);
    if (record) {
        populateForm(record, true);
        showForm();
    }
}

async function deleteHealthRecord(id) {
    if (confirm("Are you sure you want to delete this record?")) {
        try {
            await apiDelete(`/fitness-app/api/health-records/${id}`);
            showToast("Record deleted successfully", "success");
            await loadHealthRecords();
        } catch (error) {
            console.error(`Error deleting health record ${id}:`, error);
            showToast("Error deleting record", "error");
        }
    }
}

function renderTable(records, page) {
    const tableBody = document.getElementById('health-record-table-body');
    tableBody.innerHTML = '';

    if (records.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="9" class="text-center">No health records found</td></tr>';
        return;
    }

    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    const paginatedRecords = records.slice(start, end);

    paginatedRecords.forEach(record => {
        const row = document.createElement('tr');
        const statusBadge = record.status === 'ACTIVE' ? '<span class="badge text-bg-success">ACTIVE</span>' : `<span class="badge text-bg-danger">${record.status}</span>`;
        row.innerHTML = `
            <td>${record.weight || 'N/A'}</td>
            <td>${record.sleepHours || 'N/A'}</td>
            <td>${record.waterIntake || 'N/A'}</td>
            <td>${record.bmi || 'N/A'}</td>
            <td>${record.heartRate || 'N/A'}</td>
            <td>${record.steps || 'N/A'}</td>
            <td>${new Date(record.recordDateTime).toLocaleString()}</td>
            <td>${statusBadge}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="editHealthRecord(${record.id})"><i class="bi bi-pencil"></i></button>
                <button class="btn btn-sm btn-info" onclick="viewHealthRecord(${record.id})"><i class="bi bi-eye"></i></button>
                <button class="btn btn-sm btn-danger" onclick="deleteHealthRecord(${record.id})"><i class="bi bi-trash"></i></button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function setupPagination(records) {
    const paginationElement = document.getElementById('pagination');
    paginationElement.innerHTML = '';
    const pageCount = Math.ceil(records.length / rowsPerPage);

    for (let i = 1; i <= pageCount; i++) {
        const li = document.createElement('li');
        li.className = `page-item ${i === currentPage ? 'active' : ''}`;
        li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        li.addEventListener('click', (e) => {
            e.preventDefault();
            currentPage = i;
            renderTable(records, currentPage);
        });
        paginationElement.appendChild(li);
    }
}

function setupSearch() {
    const searchInput = document.getElementById('table-search');
    searchInput.addEventListener('input', () => {
        const searchTerm = searchInput.value.toLowerCase();
        const filteredRecords = allHealthRecords.filter(record =>
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
    document.getElementById('healthRecordId').value = record.id || '';
    document.getElementById('weight').value = record.weight || '';
    document.getElementById('sleepHours').value = record.sleepHours || '';
    document.getElementById('waterIntake').value = record.waterIntake || '';
    document.getElementById('bmi').value = record.bmi || '';
    document.getElementById('heartRate').value = record.heartRate || '';
    document.getElementById('steps').value = record.steps || '';
    document.getElementById('recordDateTime').value = record.recordDateTime ? new Date(record.recordDateTime).toISOString().slice(0, 16) : '';
    document.getElementById('status').value = record.status || 'ACTIVE';

    const formElements = document.getElementById('health-record-form').elements;
    for (let i = 0; i < formElements.length; i++) {
        formElements[i].disabled = isReadOnly;
    }
    document.querySelector('#health-record-form button[type="submit"]').style.display = isReadOnly ? 'none' : 'block';
    document.getElementById('form-title').innerText = isReadOnly ? 'View Health Record' : (record.id ? 'Edit Health Record' : 'Add Health Record');
}

function showList() {
    document.getElementById('health-record-list-card').style.display = 'block';
    document.getElementById('health-record-form-card').style.display = 'none';
}

function showForm(isAdd = false) {
    document.getElementById('health-record-list-card').style.display = 'none';
    document.getElementById('health-record-form-card').style.display = 'block';
    if (isAdd) {
        document.getElementById('health-record-form').reset();
        document.getElementById('healthRecordId').value = '';
        document.getElementById('status').value = 'ACTIVE';
        document.getElementById('form-title').innerText = 'Add Health Record';
        const formElements = document.getElementById('health-record-form').elements;
        for (let i = 0; i < formElements.length; i++) {
            formElements[i].disabled = false;
        }
        document.querySelector('#health-record-form button[type="submit"]').style.display = 'block';
    }
}

document.getElementById('health-record-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const user = getUserDetails();
    const record = {
        id: document.getElementById('healthRecordId').value || null,
        userId: user.id,
        weight: document.getElementById('weight').value,
        sleepHours: document.getElementById('sleepHours').value,
        waterIntake: document.getElementById('waterIntake').value,
        bmi: document.getElementById('bmi').value,
        heartRate: document.getElementById('heartRate').value,
        steps: document.getElementById('steps').value,
        recordDateTime: new Date(document.getElementById('recordDateTime').value).toISOString(),
        status: document.getElementById('status').value,
    };

    try {
        await apiPost('/fitness-app/api/health-records', record);
        showToast('Health record saved successfully', 'success');
        showList();
        loadHealthRecords();
    } catch (error) {
        console.error('Error saving health record:', error);
        showToast('Error saving health record', 'error');
    }
});

document.addEventListener('componentsLoaded', loadHealthRecords);

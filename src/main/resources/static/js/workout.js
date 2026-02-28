if (!getToken()) {
    window.location.href = "/fitness-app/page/login.html";
}

let allWorkouts = [];
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

async function loadWorkouts() {
    try {
        allWorkouts = await apiGet("/fitness-app/api/workouts/by-user/" + getUserId());
        renderTable(allWorkouts, currentPage);
        setupPagination(allWorkouts);
        setupSearch();
        await loadLookupData(); // Ensure lookups are loaded before populating forms
    } catch (error) {
        console.error("Error loading workouts:", error);
    }
}

async function fetchWorkout(id) {
    try {
        return await apiGet(`/fitness-app/api/workouts/${id}`);
    } catch (error) {
        console.error(`Error fetching workout ${id}:`, error);
        return null;
    }
}

async function editWorkout(id) {
    const workout = await fetchWorkout(id);
    if (workout) {
        populateForm(workout, false);
        showForm();
    }
}

async function viewWorkout(id) {
    const workout = await fetchWorkout(id);
    if (workout) {
        populateForm(workout, true);
        showForm();
    }
}

async function deleteWorkout(id) {
    if (confirm("Are you sure you want to delete this workout?")) {
        try {
            await apiDelete(`/fitness-app/api/workouts/${id}`);
            showToast("Workout deleted successfully", "success");
            loadWorkouts();
        } catch (error) {
            console.error(`Error deleting workout ${id}:`, error);
            showToast("Error deleting workout", "danger");
        }
    }
}

function renderTable(workouts, page) {
    const tableBody = document.getElementById('workout-table-body');
    tableBody.innerHTML = '';

    if (!workouts || workouts.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="8" class="text-center">No workouts found</td></tr>';
        document.getElementById('page-info').innerText = '0 of 0';
        return;
    }

    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    const paginatedWorkouts = workouts.slice(start, end);

    paginatedWorkouts.forEach(workout => {
        const row = document.createElement('tr');
        row.classList.add('align-left');
        const statusBadge = workout.status === 'ACTIVE' ? '<span class="badge text-bg-success">ACTIVE</span>' : `<span class="badge text-bg-danger">${workout.status}</span>`;
        row.innerHTML = `
            <td>${workout.exerciseName}</td>
            <td>${workout.typeValue || 'N/A'}</td>
            <td>${workout.duration} ${workout.durationUomValue || ''}</td>
            <td>${workout.intensityValue || 'N/A'}</td>
            <td>${workout.caloriesBurned}</td>
            <td>${new Date(workout.recordDateTime).toLocaleString()}</td>
            <td>${statusBadge}</td>
            <td>
                <div class="d-flex justify-content-center gap-2">
                    <button class="btn btn-sm btn-light border text-primary rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="viewWorkout(${workout.id})"
                            title="View">
                        <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-light border text-secondary rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="editWorkout(${workout.id})"
                            title="Edit">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-light border text-danger rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="deleteWorkout(${workout.id})"
                            title="Delete">
                        <i class="bi bi-trash"></i>
                    </button>
                </div>
            </td>
        `;
        tableBody.appendChild(row);
    });

    const totalWorkouts = workouts.length;
    const startRange = start + 1;
    const endRange = Math.min(end, totalWorkouts);
    document.getElementById('page-info').innerText = `${startRange}-${endRange} of ${totalWorkouts}`;
}

function setupPagination(workouts) {
    const paginationElement = document.getElementById('pagination');
    paginationElement.innerHTML = '';
    if (!workouts || workouts.length === 0) return;

    const pageCount = Math.ceil(workouts.length / rowsPerPage);
    for (let i = 1; i <= pageCount; i++) {
        const li = document.createElement('li');
        li.className = `page-item ${i === currentPage ? 'active' : ''}`;
        li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        li.addEventListener('click', (e) => {
            e.preventDefault();
            currentPage = i;
            renderTable(workouts, currentPage);
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
        const filteredWorkouts = allWorkouts.filter(workout =>
            Object.values(workout).some(value =>
                String(value).toLowerCase().includes(searchTerm)
            )
        );
        currentPage = 1;
        renderTable(filteredWorkouts, currentPage);
        setupPagination(filteredWorkouts);
    });
}

function populateForm(workout, isReadOnly) {
    document.getElementById('workoutId').value = workout.id || '';
    document.getElementById('exerciseName').value = workout.exerciseName || '';
    
    if (workout.typeId) {
        ensureOption('typeId', workout.typeId, workout.typeValue);
    }
    if (workout.durationUomId) {
        ensureOption('durationUomId', workout.durationUomId, workout.durationUomValue);
    }
    if (workout.intensityId) {
        ensureOption('intensityId', workout.intensityId, workout.intensityValue);
    }

    document.getElementById('typeId').value = workout.typeId || '';
    document.getElementById('duration').value = workout.duration || '';
    document.getElementById('durationUomId').value = workout.durationUomId || '';
    document.getElementById('intensityId').value = workout.intensityId || '';
    document.getElementById('caloriesBurned').value = workout.caloriesBurned || '';
    document.getElementById('recordDateTime').value = workout.recordDateTime ? new Date(workout.recordDateTime).toISOString().slice(0, 16) : '';
    document.getElementById('notes').value = workout.notes || '';
    document.getElementById('status').value = workout.status || 'ACTIVE';

    const formElements = document.getElementById('workout-form').elements;
    for (let i = 0; i < formElements.length; i++) {
        formElements[i].disabled = isReadOnly;
    }
    document.querySelector('#workout-form button[type="submit"]').style.display = isReadOnly ? 'none' : 'block';
    document.getElementById('form-title').innerText = isReadOnly ? 'View Workout' : (workout.id ? 'Edit Workout' : 'Add Workout');
}

function showList() {
    document.getElementById('workout-list-card').style.display = 'block';
    document.getElementById('workout-form-card').style.display = 'none';
}

function showForm(isAdd = false) {
    document.getElementById('workout-list-card').style.display = 'none';
    document.getElementById('workout-form-card').style.display = 'block';
    if (isAdd) {
        document.getElementById('workout-form').reset();
        document.getElementById('workoutId').value = '';
        document.getElementById('status').value = 'ACTIVE'; // Set default status for new records
        document.getElementById('form-title').innerText = 'Add Workout';
        const formElements = document.getElementById('workout-form').elements;
        for (let i = 0; i < formElements.length; i++) {
            formElements[i].disabled = false;
        }
        document.querySelector('#workout-form button[type="submit"]').style.display = 'block';
    }
}

async function loadLookupData() {
    try {
        const data = await apiGet("/fitness-app/api/lookups/by-status-active?groupKeys=WORKOUT_TYPE~DURATION_UOM~WORKOUT_INTENSITY");
        if (!data) return;

        populateSelect('typeId', data.WORKOUT_TYPE || []);
        populateSelect('durationUomId', data.DURATION_UOM || []);
        populateSelect('intensityId', data.WORKOUT_INTENSITY || []);
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

document.getElementById('workout-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const workout = {
        id: document.getElementById('workoutId').value || null,
        userId: getUserId(),
        exerciseName: document.getElementById('exerciseName').value,
        typeId: document.getElementById('typeId').value,
        duration: document.getElementById('duration').value,
        durationUomId: document.getElementById('durationUomId').value,
        intensityId: document.getElementById('intensityId').value,
        caloriesBurned: document.getElementById('caloriesBurned').value,
        recordDateTime: new Date(document.getElementById('recordDateTime').value).toISOString(),
        notes: document.getElementById('notes').value,
        status: document.getElementById('status').value,
    };

    try {
        await apiPost('/fitness-app/api/workouts', workout);
        showToast('Workout saved successfully', 'success');
        showList();
        loadWorkouts();
    } catch (error) {
        console.error('Error saving workout:', error);
        showToast('Error saving workout', 'danger');
    }
});

document.addEventListener('componentsLoaded', loadWorkouts);

if (!getToken()) {
      window.location.href = "/fitness-app/page/login.html";
}

let allLookups = [];
let currentPage = 1;
const rowsPerPage = 10;
const loggedInUser = getUserDetails();
const isAdmin = loggedInUser && loggedInUser.role === 'ADMIN';

async function loadLookups() {
      if (!isAdmin) {
            document.getElementById('lookup-list-card').style.display = 'none';
            document.getElementById('unauthorized-message-card').style.display = 'block';
            return;
      }

      try {
            allLookups = await apiGet('/api/lookups');
            renderTable(allLookups, currentPage);
            setupPagination(allLookups);
            setupSearch();
      } catch (error) {
            console.error("Error loading lookups:", error);
            showToast("Error loading lookups!", "danger");
      }
}

async function fetchLookup(id) {
      try {
            return await apiGet(`/api/lookups/${id}`);
      } catch (error) {
            console.error(`Error fetching lookup ${id}:`, error);
            return null;
      }
}

async function editLookup(id) {
      const lookup = await fetchLookup(id);
      if (lookup) {
            populateForm(lookup, false);
            showForm();
      }
}

async function viewLookup(id) {
      const lookup = await fetchLookup(id);
      if (lookup) {
            populateForm(lookup, true);
            showForm();
      }
}

function updateLookup(lookup) {
      return apiPost('/api/lookups', lookup);
}

function renderTable(lookups, page) {
      const tableBody = document.getElementById('lookup-table-body');
      tableBody.innerHTML = '';

      if (!lookups || lookups.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="4" class="text-center">No lookups found</td></tr>';
            document.getElementById('page-info').innerText = '0 of 0';
            return;
      }

      const start = (page - 1) * rowsPerPage;
      const end = start + rowsPerPage;
      const paginatedLookups = lookups.slice(start, end);

      paginatedLookups.forEach(lookup => {
            const row = document.createElement('tr');
            row.classList.add('align-left');

            let statusBadge = '';
            if (lookup.status === 'ACTIVE') {
                  statusBadge = '<span class="badge text-bg-success">ACTIVE</span>';
            } else {
                  statusBadge = `<span class="badge text-bg-danger">${lookup.status}</span>`;
            }

            row.innerHTML = `
            <td>${lookup.groupKey}</td>
            <td>${lookup.value}</td>
            <td>${statusBadge}</td>
            <td>
                <div class="d-flex justify-content-center gap-2">
                    <button class="btn btn-sm btn-light border text-primary rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="editLookup(${lookup.id})"
                            title="Edit">
                            
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-light border text-secondary rounded-circle"
                            style="width:36px; height:36px;"
                            onclick="viewLookup(${lookup.id})"
                            title="View">
                        <i class="bi bi-eye"></i>
                    </button>
                </div>
            </td>
        `;
            tableBody.appendChild(row);
      });

      const totalLookups = lookups.length;
      const startRange = start + 1;
      const endRange = Math.min(end, totalLookups);
      document.getElementById('page-info').innerText = `${startRange}-${endRange} of ${totalLookups}`;
}

function setupPagination(lookups) {
      const paginationElement = document.getElementById('pagination');
      paginationElement.innerHTML = '';
      if (!lookups || lookups.length === 0) return;

      const pageCount = Math.ceil(lookups.length / rowsPerPage);
      for (let i = 1; i <= pageCount; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${i === currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
            li.addEventListener('click', (e) => {
                  e.preventDefault();
                  currentPage = i;
                  renderTable(lookups, currentPage);
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
            const filteredLookups = allLookups.filter(lookup =>
                lookup.groupKey.toLowerCase().includes(searchTerm) ||
                lookup.value.toLowerCase().includes(searchTerm) ||
                lookup.status.toLowerCase().includes(searchTerm)
            );
            currentPage = 1;
            renderTable(filteredLookups, currentPage);
            setupPagination(filteredLookups);
      });
}

function populateForm(lookup, isReadOnly) {
      document.getElementById('lookupId').value = lookup.id;
      document.getElementById('groupKey').value = lookup.groupKey;
      document.getElementById('value').value = lookup.value;
      document.getElementById('status').value = lookup.status;

      const formElements = document.getElementById('lookup-form').elements;
      for (let i = 0; i < formElements.length; i++) {
            formElements[i].disabled = isReadOnly;
      }

      const buttons = document.querySelectorAll('#lookup-form button[type="button"]');
      buttons.forEach(btn => btn.disabled = false);

      const saveBtn = document.getElementById('save-btn');
      if (isReadOnly) {
            saveBtn.style.display = 'none';
            document.getElementById('form-title').innerText = 'View Lookup';
      } else {
            saveBtn.style.display = 'block';
            saveBtn.disabled = false;
            validateLookupForm(); // Validate initially when editing
            document.getElementById('form-title').innerText = 'Edit Lookup';
      }
}

function showList() {
      document.getElementById('lookup-list-card').style.display = 'block';
      document.getElementById('lookup-form-card').style.display = 'none';
      document.getElementById('unauthorized-message-card').style.display = 'none';
}

function showForm(isAdd = false) {
      document.getElementById('lookup-list-card').style.display = 'none';
      document.getElementById('lookup-form-card').style.display = 'block';
      document.getElementById('unauthorized-message-card').style.display = 'none';

      const formTitle = document.getElementById('form-title');
      const saveBtn = document.getElementById('save-btn');

      if (isAdd) {
            document.getElementById('lookup-form').reset();
            document.getElementById('lookupId').value = null;

            formTitle.innerText = 'Add Lookup';
            saveBtn.innerText = 'Save';
            const formElements = document.getElementById('lookup-form').elements;
            for (let i = 0; i < formElements.length; i++) {
                  formElements[i].disabled = false;
            }
            saveBtn.style.display = 'block';
            validateLookupForm(); // Validate initially when adding
      }
}

function validateLookupForm() {
      const groupKey = document.getElementById('groupKey').value.trim();
      const value = document.getElementById('value').value.trim();
      const status = document.getElementById('status').value;

      const isValid = groupKey && value && status;
      document.getElementById('save-btn').disabled = !isValid;
}

// Add event listeners for validation
const formInputs = ['groupKey', 'value', 'status'];
formInputs.forEach(id => {
      const element = document.getElementById(id);
      if (element) {
            element.addEventListener('input', validateLookupForm);
            element.addEventListener('change', validateLookupForm);
      }
});

document.getElementById('lookup-form').addEventListener('submit', async function (event) {
      event.preventDefault();

      const id = document.getElementById('lookupId').value;
      const lookup = {
            id: id || null,
            groupKey: document.getElementById('groupKey').value,
            value: document.getElementById('value').value,
            status: document.getElementById('status').value,
      };

      try {
            await updateLookup(lookup);
            showToast('Lookup updated successfully!', 'success');
            showList();
            loadLookups();
      } catch (error) {
            console.error("Error saving lookup:", error);
            showToast("Error saving lookup!", "danger");
      }
});

document.addEventListener('componentsLoaded', loadLookups);

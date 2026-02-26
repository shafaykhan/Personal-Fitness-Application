if (!getToken()) {
      window.location.href = "/fitness-app/page/login.html";
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

// This function will set up the form and load all necessary data.
function initializePage() {
      const form = document.getElementById('user-preference-form');
      const newForm = form.cloneNode(true);
      form.parentNode.replaceChild(newForm, form);
      newForm.addEventListener('submit', handleUserPreferenceSubmit);

      loadInitialData();
}

async function handleUserPreferenceSubmit(event) {
      event.preventDefault();
      await saveUserPreference();
}

async function loadInitialData() {
      await loadLookupData();
      await loadUserPreference();
}

async function loadLookupData() {
      try {
            const data = await apiGet("/fitness-app/api/lookups/by-status-active?groupKeys=DIET_TYPE~WORKOUT_INTENSITY~GOAL_TYPE");
            populateDropdown('diet-type', data.DIET_TYPE || []);
            populateDropdown('workout-intensity', data.WORKOUT_INTENSITY || []);
            populateDropdown('goal-type', data.GOAL_TYPE || []);
      } catch (error) {
            console.error("Error loading lookup data:", error);
            showToast('Error loading selection options.', 'error');
      }
}

function populateDropdown(selectId, items) {
      const select = document.getElementById(selectId);
      const currentValue = select.value;
      select.innerHTML = '<option value="">-- Select --</option>';
      if (Array.isArray(items)) {
            items.forEach(item => {
                  const option = document.createElement('option');
                  option.value = item.id;
                  option.textContent = item.value;
                  select.appendChild(option);
            });
      }
      if (currentValue) {
            select.value = currentValue;
      }
}

async function loadUserPreference() {
      try {
            const data = await apiGet("/fitness-app/api/user-preferences/by-user/" + getUserId());
            if (data) {
                  populateForm(data);
            }
      } catch (error) {
            if (error.response && error.response.status === 404) {
                  showToast("Welcome! Please set your preferences.", "info");
            } else {
                  console.error("Error loading user preferences:", error);
                  showToast("Could not load your preferences.", "error");
            }
      }
}

function populateForm(data) {
      document.getElementById('preferenceId').value = data.id || '';
      document.getElementById('userId').value = data.userId || getUserId();

      ensureOption('diet-type', data.dietTypeId, data.dietTypeValue);
      document.getElementById('diet-type').value = data.dietTypeId || '';

      ensureOption('workout-intensity', data.intensityId, data.intensityValue);
      document.getElementById('workout-intensity').value = data.intensityId || '';

      ensureOption('goal-type', data.goalTypeId, data.goalTypeValue);
      document.getElementById('goal-type').value = data.goalTypeId || '';

      document.getElementById('allergies').value = data.allergies || '';
}

async function saveUserPreference() {
      const payload = {
            id: document.getElementById('preferenceId').value || null,
            userId: document.getElementById('userId').value || getUserId(),
            dietTypeId: document.getElementById('diet-type').value,
            intensityId: document.getElementById('workout-intensity').value,
            goalTypeId: document.getElementById('goal-type').value,
            allergies: document.getElementById('allergies').value.trim()
      };

      try {
            await apiPost("/fitness-app/api/user-preferences", payload);
            showToast("Preferences saved successfully!", "success");
      } catch (error) {
            console.error("Error saving user preference:", error);
            showToast("Failed to save preferences. Please try again.", "error");
      }
}

window.addEventListener('pageshow', function (event) {
      if (event.persisted) {
            initializePage();
      }
});

document.addEventListener('DOMContentLoaded', initializePage);

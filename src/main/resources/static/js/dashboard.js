if (!getToken()) {
      window.location.href = "/fitness-app/page/login.html";
}

async function loadDashboard() {
      const userId = getUserId();
      if (!userId) return;

      await Promise.all([
            fetchRecentWorkouts(userId),
            fetchRecentHealthRecords(userId),
            fetchRecentDietRecords(userId),
            fetchRecentGoals(userId)
      ]);
}

async function fetchRecentWorkouts(userId) {
      try {
            const workouts = await apiGet(`/api/workouts/by-user/${userId}`);
            const recentWorkouts = workouts
                  .sort((a, b) => new Date(b.recordDateTime) - new Date(a.recordDateTime))
                  .slice(0, 3);
            renderList('workout-list', recentWorkouts, workout => `
                  <tr>
                        <td><strong>${workout.exerciseName}</strong></td>
                        <td><span class="badge text-bg-info opacity-75">${workout.typeValue || '-'}</span></td>
                        <td><i class="bi bi-clock-history me-1 text-muted"></i>${workout.duration} ${workout.durationUomValue || ''}</td>
                        <td class="text-muted small">${new Date(workout.recordDateTime).toLocaleDateString()}</td>
                  </tr>
            `, 4);
      } catch (error) {
            console.error('Error fetching workouts:', error);
      }
}

async function fetchRecentHealthRecords(userId) {
      try {
            const records = await apiGet(`/api/health-records/by-user/${userId}`);
            const recentRecords = records
                  .sort((a, b) => new Date(b.recordDateTime) - new Date(a.recordDateTime))
                  .slice(0, 3);
            renderList('health-list', recentRecords, record => `
                  <tr>
                        <td><i class="bi bi-scale me-1 text-muted"></i>${record.weight || '-'} <small>kg</small></td>
                        <td><i class="bi bi-moon-stars me-1 text-muted"></i>${record.sleepHours || '-'} <small>hrs</small></td>
                        <td><i class="bi bi-person-walking me-1 text-muted"></i>${record.steps || '-'}</td>
                        <td class="text-muted small">${new Date(record.recordDateTime).toLocaleDateString()}</td>
                  </tr>
            `, 4);
      } catch (error) {
            console.error('Error fetching health records:', error);
      }
}

async function fetchRecentDietRecords(userId) {
      try {
            const records = await apiGet(`/api/diet-records/by-user/${userId}`);
            const recentRecords = records
                  .sort((a, b) => new Date(b.recordDateTime) - new Date(a.recordDateTime))
                  .slice(0, 3);
            renderList('diet-list', recentRecords, record => `
                  <tr>
                        <td><strong>${record.foodName}</strong></td>
                        <td><span class="badge text-bg-warning opacity-75">${record.mealTypeValue || '-'}</span></td>
                        <td><i class="bi bi-fire me-1 text-danger"></i>${record.calories || '-'} <small>kcal</small></td>
                        <td class="text-muted small">${new Date(record.recordDateTime).toLocaleDateString()}</td>
                  </tr>
            `, 4);
      } catch (error) {
            console.error('Error fetching diet records:', error);
      }
}

async function fetchRecentGoals(userId) {
      try {
            const goals = await apiGet(`/api/goals/by-user/${userId}`);
            const recentGoals = goals
                  .sort((a, b) => new Date(b.recordDateTime) - new Date(a.recordDateTime))
                  .slice(0, 3);
            renderList('goal-list', recentGoals, goal => {
                  const progress = Math.min(Math.round((goal.currentValue / goal.targetValue) * 100), 100);
                  const progressColor = progress >= 100 ? 'bg-success' : 'bg-primary';
                  return `
                  <tr>
                        <td><strong>${goal.typeValue || '-'}</strong></td>
                        <td>${goal.targetValue}</td>
                        <td style="min-width: 150px;">
                              <div class="d-flex align-items-center">
                                    <div class="progress flex-grow-1 me-2" style="height: 6px;">
                                          <div class="progress-bar ${progressColor}" role="progressbar" style="width: ${progress}%" aria-valuenow="${progress}" aria-valuemin="0" aria-valuemax="100"></div>
                                    </div>
                                    <small class="text-muted">${progress}%</small>
                              </div>
                        </td>
                        <td><span class="badge ${goal.status === 'ACTIVE' ? 'text-bg-success' : 'text-bg-secondary'}">${goal.status}</span></td>
                  </tr>
            `;}, 4);
      } catch (error) {
            console.error('Error fetching goals:', error);
      }
}

function renderList(elementId, items, templateFn, colSpan = 3) {
      const container = document.getElementById(elementId);
      if (!container) return;

      let html = items.map(templateFn).join('');
      
      // Add empty rows to reach 5 total rows for consistent UI height
      for (let i = items.length; i < 3; i++) {
            html += `<tr><td colspan="${colSpan}" style="height: 48px;">&nbsp;</td></tr>`;
      }

      container.innerHTML = html;
}

// Listen for the custom event dispatched by common.js
document.addEventListener('componentsLoaded', loadDashboard);

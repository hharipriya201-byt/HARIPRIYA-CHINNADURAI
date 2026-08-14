let subjects = JSON.parse(localStorage.getItem("studentSubjects")) || [];
let tasks = JSON.parse(localStorage.getItem("studentTasks")) || [];

const subjectForm = document.getElementById("subjectForm");
const taskForm = document.getElementById("taskForm");

function save() {
  localStorage.setItem("studentSubjects", JSON.stringify(subjects));
  localStorage.setItem("studentTasks", JSON.stringify(tasks));
}

function render() {
  renderSubjects();
  renderTasks();
  updateStats();
}

function renderSubjects() {
  const container = document.getElementById("subjects");
  container.innerHTML = "";

  document.getElementById("noSubjects").style.display =
    subjects.length ? "none" : "block";

  subjects.forEach(subject => {
    const percentage = Math.round((subject.attended / subject.total) * 100);
    const status = percentage < 75 ? "warning" : "good";

    const item = document.createElement("div");
    item.className = "subject";
    item.innerHTML = `
      <div class="subject-top">
        <span class="subject-name">${escapeHtml(subject.name)}</span>
        <span class="${status}">${percentage}%</span>
      </div>
      <div class="progress">
        <div class="progress-bar" style="width:${Math.min(percentage, 100)}%"></div>
      </div>
      <div class="subject-top">
        <small>${subject.attended} of ${subject.total} classes attended</small>
        <button class="delete" onclick="deleteSubject('${subject.id}')">Delete</button>
      </div>
    `;
    container.appendChild(item);
  });
}

function renderTasks() {
  const container = document.getElementById("tasks");
  container.innerHTML = "";

  document.getElementById("noTasks").style.display =
    tasks.length ? "none" : "block";

  tasks.forEach(task => {
    const item = document.createElement("div");
    item.className = `task ${task.completed ? "completed" : ""}`;

    item.innerHTML = `
      <div class="task-info">
        <strong>${escapeHtml(task.name)}</strong>
        <small>Due: ${escapeHtml(task.due)}</small>
      </div>
      <div class="task-actions">
        <button class="complete" onclick="toggleTask('${task.id}')">
          ${task.completed ? "Undo" : "Complete"}
        </button>
        <button class="delete" onclick="deleteTask('${task.id}')">Delete</button>
      </div>
    `;

    container.appendChild(item);
  });
}

function updateStats() {
  const totalClasses = subjects.reduce((sum, s) => sum + s.total, 0);
  const attendedClasses = subjects.reduce((sum, s) => sum + s.attended, 0);
  const percentage = totalClasses
    ? Math.round((attendedClasses / totalClasses) * 100)
    : 0;

  document.getElementById("overallAttendance").textContent = `${percentage}%`;
  document.getElementById("subjectCount").textContent = subjects.length;
  document.getElementById("pendingCount").textContent =
    tasks.filter(t => !t.completed).length;
  document.getElementById("completedCount").textContent =
    tasks.filter(t => t.completed).length;
}

subjectForm.addEventListener("submit", event => {
  event.preventDefault();

  const name = document.getElementById("subjectName").value.trim();
  const attended = Number(document.getElementById("attended").value);
  const total = Number(document.getElementById("totalClasses").value);

  if (!name || total <= 0 || attended < 0 || attended > total) {
    alert("Please enter valid attendance values.");
    return;
  }

  subjects.push({
    id: crypto.randomUUID(),
    name,
    attended,
    total
  });

  save();
  render();
  subjectForm.reset();
  document.getElementById("attended").value = 0;
  document.getElementById("totalClasses").value = 1;
});

taskForm.addEventListener("submit", event => {
  event.preventDefault();

  const name = document.getElementById("taskName").value.trim();
  const due = document.getElementById("dueDate").value;

  if (!name || !due) return;

  tasks.push({
    id: crypto.randomUUID(),
    name,
    due,
    completed: false
  });

  save();
  render();
  taskForm.reset();
});

function deleteSubject(id) {
  subjects = subjects.filter(subject => subject.id !== id);
  save();
  render();
}

function deleteTask(id) {
  tasks = tasks.filter(task => task.id !== id);
  save();
  render();
}

function toggleTask(id) {
  tasks = tasks.map(task =>
    task.id === id ? {...task, completed: !task.completed} : task
  );
  save();
  render();
}

document.getElementById("clearSubjects").addEventListener("click", () => {
  if (subjects.length && confirm("Clear all subjects?")) {
    subjects = [];
    save();
    render();
  }
});

document.getElementById("clearTasks").addEventListener("click", () => {
  if (tasks.length && confirm("Clear all tasks?")) {
    tasks = [];
    save();
    render();
  }
});

function escapeHtml(value) {
  return String(value).replace(/[&<>"']/g, char => ({
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': "&quot;",
    "'": "&#039;"
  }[char]));
}

render();

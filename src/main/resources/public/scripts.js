const weekGrid = document.getElementById('week-grid');
const taskForm = document.getElementById('task-form');
const deleteButton = document.getElementById('delete-button');
const cancelButton = document.getElementById('cancel-button');
let currentWeekStart = getStartOfWeek(new Date());
let currentWeekEnd = new Date(currentWeekStart);
currentWeekEnd.setDate(currentWeekEnd.getDate() + 6);
cancelButton.style.display = 'none';
let editingTaskId = null;
const today = new Date();

document.addEventListener("DOMContentLoaded", function () {
    if (!weekGrid) {
        console.error("Element with id 'week-grid' not found. Check your HTML structure.");
        return;
    }

    fetchTasks();
    createWeeklyView();

    taskForm.addEventListener("submit", function (event) {
        event.preventDefault();
        editingTaskId ? updateTask(editingTaskId) : addTask();
    });

    deleteButton.addEventListener("click", function () {
        if (editingTaskId) deleteTask(editingTaskId);
    });

    cancelButton.addEventListener("click", function () {
        resetForm();
    });
});

document.getElementById("prev-week").addEventListener("click", function() {
    // Move the reference date back 7 days
    currentWeekStart.setDate(currentWeekStart.getDate() - 7);
    currentWeekEnd = new Date(currentWeekStart);
	currentWeekEnd.setDate(currentWeekStart.getDate() + 6);
    createWeeklyView();
    fetchTasks(); // or however you load tasks
});

document.getElementById("next-week").addEventListener("click", function() {
    // Move the reference date forward 7 days
    currentWeekStart.setDate(currentWeekStart.getDate() + 7);
    currentWeekEnd = new Date(currentWeekStart);
	currentWeekEnd.setDate(currentWeekStart.getDate() + 6);
    createWeeklyView();
    fetchTasks();
});


function getStartOfWeek(date) {
  const day = date.getDay();  // 0=Sunday, 1=Monday, etc.
  // Move date backward by 'day' days, so Sunday becomes day 0
  date.setDate(date.getDate() - day);
  return date;
}

function createWeeklyView() {
    weekGrid.innerHTML = '';

    for (let i = 0; i < 7; i++) {
        const currentDate = new Date(currentWeekStart);
        currentDate.setDate(currentDate.getDate() + i);

        const dayColumn = document.createElement('div');
        dayColumn.className = 'day-column';

		if (currentDate.toDateString() === today.toDateString()) {
			// Add a CSS class
			dayColumn.classList.add("today-column");
		}
        const dayHeader = document.createElement('div');
        dayHeader.className = 'day-header';
        dayHeader.textContent = currentDate.toLocaleDateString('en-US', { weekday: 'long', month: 'short', day: 'numeric' });

        const taskList = document.createElement('div');
        taskList.className = 'task-list';
        taskList.id = `day-${i}`;

        dayColumn.appendChild(dayHeader);
        dayColumn.appendChild(taskList);
        weekGrid.appendChild(dayColumn);
    }
}

function formatTimeTo12Hour(timeSlot) {
    if (Array.isArray(timeSlot) && timeSlot.length === 2) {
        const [hours, minutes] = timeSlot;
        const ampm = hours >= 12 ? 'PM' : 'AM';
        const formattedHours = (hours % 12) || 12;
        return `${formattedHours}:${minutes.toString().padStart(2, '0')} ${ampm}`;
    } else if (typeof timeSlot === 'string') {
        const [hours, minutes] = timeSlot.split(':').map(Number);
        const ampm = hours >= 12 ? 'PM' : 'AM';
        const formattedHours = (hours % 12) || 12;
        return `${formattedHours}:${minutes.toString().padStart(2, '0')} ${ampm}`;
    }
    return '--:--';
}

function populateTasks(tasks) {
    document.querySelectorAll('.task-list').forEach(list => list.innerHTML = ''); // Clear existing tasks

    tasks.forEach(task => {
        const taskDate = new Date(task.date[0], task.date[1] - 1, task.date[2]);
        if (taskDate >= currentWeekStart && taskDate <= currentWeekEnd) {
	        const taskElement = document.createElement('div');
	        taskElement.className = 'task';
	        taskElement.style.backgroundColor = task.color;
	        taskElement.textContent = `${task.name} (${formatTimeTo12Hour(task.timeSlot)})`;
	
	        const dayIndex = taskDate.getDay();
	        const taskList = document.getElementById(`day-${dayIndex}`);
	
	        if (taskList) {
	            taskElement.onclick = () => fillFormForEdit(task);
	            taskList.appendChild(taskElement);
	        } else {
	            console.warn(`Task list for day-${dayIndex} not found.`);
	        }
	     }
    });
}

function fillFormForEdit(task) {
    document.getElementById('task-name').value = task.name;
    document.getElementById('task-date').value = `${task.date[0]}-${String(task.date[1]).padStart(2, '0')}-${String(task.date[2]).padStart(2, '0')}`;
	document.getElementById("task-desc").value = task.description || "";
    if (Array.isArray(task.timeSlot) && task.timeSlot.length === 2) {
        const [hours, minutes] = task.timeSlot;
        document.getElementById('task-time').value = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;
    } else if (typeof task.timeSlot === 'string') {
        const [hours, minutes] = task.timeSlot.split(':');
        document.getElementById('task-time').value = `${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}`;
    } else {
        document.getElementById('task-time').value = '';
    }

    document.getElementById('task-color').value = task.color;
    document.getElementById('form-title').textContent = 'Edit Task';
    editingTaskId = task.id;
    deleteButton.style.display = 'inline-block';
    cancelButton.style.display = 'inline-block';
    document.getElementById("submit-button").innerText = "Update Task";
}

function formatDate(date) {
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `${yyyy}-${mm}-${dd}`;
}

function fetchTasks() {
  	let startString = formatDate(currentWeekStart);  // "YYYY-MM-DD"
  	let endString = formatDate(currentWeekEnd);      // "YYYY-MM-DD"	
    fetch(`/tasks?start=${startString}&end=${endString}`)
        .then(response => {
			if (response.status === 401) {
			  window.location.href = "/login"; 
			}
            if (!response.ok) throw new Error(`HTTP error ${response.status}`);
            return response.json();
        })
        .then(data => {
            console.log("Fetched tasks:", data);
            populateTasks(data);
        })
        .catch(error => console.error("Error fetching tasks:", error));
}

function getFormData() {
    return {
        name: document.getElementById("task-name").value,
        date: document.getElementById("task-date").value,
        timeSlot: document.getElementById("task-time").value,
        color: document.getElementById("task-color").value,
        description: document.getElementById("task-desc").value
    };
}

function updateTask(taskId) {
    const task = getFormData();

    fetch(`/tasks/${taskId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(task)
    })
    .then(response => {
        if (response.ok) {
            fetchTasks();
            resetForm();
        } else {
            throw new Error("Failed to update task.");
        }
    })
    .catch(error => console.error("Error updating task:", error));
}

function deleteTask(taskId) {
    if (confirm("Are you sure you want to delete this task?")) {
        fetch(`/tasks/${taskId}`, { method: "DELETE" })
            .then(response => {
                if (response.ok) {
                    fetchTasks();
                    resetForm();
                } else {
                    throw new Error("Failed to delete task.");
                }
            })
            .catch(error => console.error("Error deleting task:", error));
    }
}

function addTask() {
    const task = getFormData();

    fetch("/tasks", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(task)
    })
    .then(response => {
        if (response.ok) {
            fetchTasks();
            resetForm();
        } else {
            throw new Error("Failed to add task.");
        }
    })
    .catch(error => console.error("Error adding task:", error));
}

function resetForm() {
    taskForm.reset();
    editingTaskId = null;
    document.getElementById("submit-button").innerText = "Submit";
    deleteButton.style.display = 'none';
    cancelButton.style.display = 'none';
    document.getElementById('form-title').textContent = 'Add Task';
}

document.addEventListener("DOMContentLoaded", function () {
    fetchTasks();

    document.getElementById("task-form").addEventListener("submit", function (event) {
        event.preventDefault();
        addTask();
    });
});

function fetchTasks() {
    fetch("/tasks")  // Calls Javalin API
        .then(response => response.json())
        .then(data => {
            const taskList = document.getElementById("task-list");
            taskList.innerHTML = "";  // Clear existing list

            data.forEach(task => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${task.name}</td>
                    <td>${task.date}</td>
                    <td>${task.timeSlot}</td>
                    <td><div style="width: 20px; height: 20px; background-color: ${task.color};"></div></td>
                    <td>
                        <button onclick="editTask(${task.id})">✏️ Edit</button>
                        <button onclick="deleteTask(${task.id})">🗑 Delete</button>
                    </td>
                `;
                taskList.appendChild(row);
            });
        })
        .catch(error => console.error("Error fetching tasks:", error));
}

function editTask(taskId) {
    fetch(`/tasks/${taskId}`)
        .then(response => response.json())
        .then(task => {
            document.getElementById("task-name").value = task.name;
            document.getElementById("task-date").value = task.date;
            document.getElementById("task-time").value = task.timeSlot;
            document.getElementById("task-color").value = task.color;

            document.getElementById("task-form").onsubmit = function (event) {
                event.preventDefault();
                updateTask(taskId);
            };
        })
        .catch(error => console.error("Error fetching task:", error));
}

function updateTask(taskId) {
    const task = {
        name: document.getElementById("task-name").value,
        date: document.getElementById("task-date").value,
        timeSlot: document.getElementById("task-time").value,
        color: document.getElementById("task-color").value
    };

    fetch(`/tasks/${taskId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(task)
    })
    .then(response => {
        if (response.ok) {
            fetchTasks();  // Reload the task list after updating
        }
    })
    .catch(error => console.error("Error updating task:", error));
}

function deleteTask(taskId) {
    if (confirm("Are you sure you want to delete this task?")) {
        fetch(`/tasks/${taskId}`, {
            method: "DELETE"
        })
        .then(response => {
            if (response.ok) {
                fetchTasks();  // Reload the task list after deleting
            }
        })
        .catch(error => console.error("Error deleting task:", error));
    }
}


function addTask() {
    const task = {
        name: document.getElementById("task-name").value,
        date: document.getElementById("task-date").value,
        timeSlot: document.getElementById("task-time").value,
        color: document.getElementById("task-color").value
    };

    fetch("/tasks", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(task)
    })
    .then(response => response.json())
    .then(() => {
        fetchTasks();  // Reload tasks after adding a new one
    })
    .catch(error => console.error("Error adding task:", error));
}

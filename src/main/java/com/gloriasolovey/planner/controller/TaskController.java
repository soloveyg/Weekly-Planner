package com.gloriasolovey.planner.controller;

import com.gloriasolovey.planner.model.Task;
import com.gloriasolovey.planner.model.User;
import com.gloriasolovey.planner.repository.PlannerRepository;
import com.gloriasolovey.planner.repository.UserRepository;

import io.javalin.Javalin;
import io.javalin.http.Context;

/*
 * Focused on task related endpoints
 */
public class TaskController {
	private final PlannerRepository plannerRepository = new PlannerRepository();

	private final UserRepository userRepository = new UserRepository();

	
	public void registerRoutes(Javalin app) {
        app.post("/tasks", this::addTask);
        app.put("/tasks/{id}", this::updateTask);
        app.delete("/tasks/{id}", this::deleteTask);
        app.get("/tasks/{id}", this::getTaskById);
    }
	
	//Create task
	private void addTask(Context ctx) {
		Task task;

		// Step 1: Validate and parse the request body
		try {
			task = ctx.bodyAsClass(Task.class);
		} catch (Exception e) {
			ctx.status(400).result("Invalid request body.");
			return;
		}

		// Step 2: Retrieve the user from the session
		Integer userId = ctx.sessionAttribute("userId");

		if (userId == null) {
			ctx.status(401).result("User session not found.");
			return;
		}

		// Step 3: Perform database operations
		try {
			User guest = userRepository.getUserById(userId);

			if (guest == null) {
				ctx.req().getSession().invalidate();
				ctx.status(401).result("Session expired.");
				return;
			}

			task.setUser(guest);
			plannerRepository.addTask(task);

			ctx.status(201).json(task);

		} catch (Exception e) {
			// Log the actual error on the server
			e.printStackTrace();

			// Return a generic error to the client
			ctx.status(500).result("Internal server error.");
		}
	}

		
	//Delete task
	private void deleteTask(Context ctx) {
		try {
			int id = Integer.parseInt(ctx.pathParam("id"));
			plannerRepository.deleteTask(id);
			ctx.status(204);
		} catch (Exception e) {
			ctx.status(400).result("Invalid ID");
		}
	}
	
		
	//edit task
	private void updateTask(Context ctx) {
		try {
			int id = Integer.parseInt(ctx.pathParam("id"));
			Task task = ctx.bodyAsClass(Task.class);
			plannerRepository.updateTask(id, task);
			ctx.status(200).result("Task Updated");
		} catch (Exception e) {
			ctx.status(400).result("Invalid Edit");
		}
	}
	
	private void getTaskById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Task task = plannerRepository.getTaskById(id);
        if (task != null) {
            ctx.json(task);
        } else {
            ctx.status(404).result("Task not found");
        }
    }

}

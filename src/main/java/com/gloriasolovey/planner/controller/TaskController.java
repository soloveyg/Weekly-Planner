package com.gloriasolovey.planner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gloriasolovey.planner.model.Task;
import com.gloriasolovey.planner.repository.PlannerRepository;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.json.JavalinJackson;

/*
 * Focused on task related endpoints
 */
public class TaskController {
	private final PlannerRepository plannerRepository = new PlannerRepository();

	
	public void registerRoutes(Javalin app) {
        app.post("/tasks", this::addTask);
        app.put("/tasks/{id}", this::updateTask);
        app.delete("/tasks/{id}", this::deleteTask);
        app.get("/tasks/{id}", this::getTaskById);
    }
	//Create task
	//POST 
	//assign an {id}
	
	private void addTask(Context ctx) {
		try {
			Task task = ctx.bodyAsClass(Task.class); // Now works with LocalDate & LocalTime
            plannerRepository.addTask(task);
            ctx.status(201).json(task);
			
		} catch(Exception e){
			ctx.status(400).result("Invalid request body. Please insert only valid variables");
		}
	}
		
	//Delete task
	//DELETE
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
	//PUT
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

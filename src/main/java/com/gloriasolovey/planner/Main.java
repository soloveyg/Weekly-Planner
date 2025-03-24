package com.gloriasolovey.planner;

import java.util.List;

import com.gloriasolovey.planner.controller.LoginController;
import com.gloriasolovey.planner.controller.PlannerController;
import com.gloriasolovey.planner.controller.TaskController;
import com.gloriasolovey.planner.model.Task;
import com.gloriasolovey.planner.repository.PlannerRepository;

import io.javalin.Javalin;

public class Main {

	private static void addInitialData() {
		PlannerRepository plannerRepo = new PlannerRepository();

        // Add a new task
        //Task task = new Task("Meeting", LocalDate, new LocalTime(12,0,0,0) , "blue");
        //plannerRepo.addTask(task);
        System.out.println("Task added successfully!");

        // Retrieve all tasks
        List<Task> tasks = plannerRepo.getAllTasks();
        System.out.println("All tasks: " + tasks);

        // Find a task by ID
        Task retrievedTask = plannerRepo.getTaskById(1);
        if (retrievedTask != null) {
            System.out.println("Retrieved Task: " + retrievedTask.getName());
        }
	}
	
    public static void main(String[] args) {
        // addInitialData();
    	Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");  // Serves HTML, CSS, JS from resources/public
        }).start(7000);
    	
    	app.before("/tasks", ctx -> {
    	    // This “before” handler runs BEFORE any route for "/planner"
    	    //Boolean isLoggedIn = ctx.sessionAttribute("isLoggedIn");
    		Boolean isLoggedIn = true;
    	    if (isLoggedIn == null || !isLoggedIn) {
    	        ctx.redirect("/login");
    	        ctx.status(401);
    	    }
    	});


        // Register controllers
        new TaskController().registerRoutes(app);
        new PlannerController().registerRoutes(app);
        new LoginController().registerRoutes(app);
    }
}


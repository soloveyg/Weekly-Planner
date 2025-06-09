package com.gloriasolovey.planner.controller;

import java.time.LocalDate;
import java.util.List;

import com.gloriasolovey.planner.model.Task;
import com.gloriasolovey.planner.repository.PlannerRepository;

import io.javalin.Javalin;
import io.javalin.http.Context;
/*
 * Handles general planner Endpoints
 */
public class PlannerController {
	
private final PlannerRepository plannerRepository = new PlannerRepository();
	
	public void registerRoutes(Javalin app) {
        app.get("/tasks", this::getWeekly);

    }
	
	
	//how to retrieve all tasks/ weekly tasks
	//Weekly View
	//GET
	public void getWeekly(Context ctx) {
		try {
		    String startStr = ctx.queryParam("start"); // e.g. "2025-02-24"
		    String endStr = ctx.queryParam("end");     // e.g. "2025-03-01"

		    // Parse them to LocalDate
		    LocalDate startDate = LocalDate.parse(startStr);
		    LocalDate endDate = LocalDate.parse(endStr);
		    
		    Integer userId = ctx.sessionAttribute("userId");
			List<Task> list = plannerRepository.getTasksForUserInTimeFrame(userId,
					startDate,
					endDate);
			ctx.status(200).json(list);
		} catch (Exception e) {
			ctx.status(500).result("Unable to retrieve Tasks of the week");
		}
	}
	
}

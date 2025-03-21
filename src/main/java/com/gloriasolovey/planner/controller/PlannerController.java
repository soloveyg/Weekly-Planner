package com.gloriasolovey.planner.controller;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gloriasolovey.planner.model.Task;
import com.gloriasolovey.planner.repository.PlannerRepository;
import com.gloriasolovey.planner.util.DateUtils;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.json.JavalinJackson;
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

		    // Parse them to LocalDate (if using Java 8+)
		    LocalDate startDate = LocalDate.parse(startStr);
		    LocalDate endDate = LocalDate.parse(endStr);
		    
			List<Task> list = plannerRepository.getTasksForUserInTimeFrame(1,
					startDate,
					endDate);
			ctx.status(200).json(list);
		} catch (Exception e) {
			ctx.status(500).result("Unable to retrieve Tasks of the week");
		}
	}
	
	//Monthly View 
	//Get
	
	

}

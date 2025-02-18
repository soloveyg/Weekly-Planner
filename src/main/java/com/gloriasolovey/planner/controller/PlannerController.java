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
	
	public void startServer() {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
		

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(objectMapper)); // Correct way in Javalin 5.x
        }).start(7000);
        
        app.get("/tasks", this::getWeekly);
    }
	
	
	public void registerRoutes(Javalin app) {
        app.get("/tasks", this::getWeekly);

    }
	//Input data required?
	
	//bad data input
	//404 not found or exception
	
	//how to retrieve all tasks/ weekly tasks
	//Weekly View
	//GET
	public void getWeekly(Context ctx) {
		try {
			List<Task> list = plannerRepository.getTasksForUserInTimeFrame(-1,
					DateUtils.getStartOfWeek(LocalDate.now()),
					DateUtils.getEndOfWeek(LocalDate.now()));
			ctx.status(200).json(list);
		} catch (Exception e) {
			ctx.status(500).result("Unable to retrieve Tasks of the week");
		}
	}
	
	//Monthly View 
	//Get
	
	

}

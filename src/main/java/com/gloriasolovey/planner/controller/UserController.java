package com.gloriasolovey.planner.controller;

import com.gloriasolovey.planner.model.Task;
import com.gloriasolovey.planner.model.User;
import com.gloriasolovey.planner.repository.PlannerRepository;
import com.gloriasolovey.planner.repository.UserRepository;

import io.javalin.Javalin;
import io.javalin.http.Context;

/*
 * Handles all of the user related endpoints
 */
public class UserController {
	private final UserRepository userRepository = new UserRepository();
	
	public void registerRoutes(Javalin app) {
        app.post("/users", this::addUser);

    }
	//Create a method for add new user
	
	private void addUser(Context ctx) {
		try {
			User user = ctx.bodyAsClass(User.class);
			userRepository.addUser(user);
			ctx.status(201).json(user);
		} catch(Exception e) {
			ctx.status(400).result("Cannot add the User");
		}
		
	}
	
	// Authentication
	
	//New Login
	
	//Sign In
	
	//Delete Account
	//not really needed yet

}

package com.gloriasolovey.planner;

import java.util.UUID;

import com.gloriasolovey.planner.controller.LoginController;
import com.gloriasolovey.planner.controller.PlannerController;
import com.gloriasolovey.planner.controller.TaskController;
import com.gloriasolovey.planner.model.User;
import com.gloriasolovey.planner.repository.UserRepository;

import io.javalin.Javalin;

/**
 * This class starts the application.
 */
public class Main {
    public static void main(String[] args) {
		UserRepository userRepo = new UserRepository();
        // addInitialData();
    	Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");  // Serves HTML, CSS, JS from resources/public
        }).start(getHerokuPort());
    	
    	app.before("/tasks", ctx -> {
    	    //Boolean isLoggedIn = ctx.sessionAttribute("isLoggedIn");
    		
    		Integer userId = ctx.sessionAttribute("userId");
    		
    	    if (userId == null) {
    	    	String guestEmail = "GUEST-"+ UUID.randomUUID().toString();
    	    	User guestUser = new User();
    	    	guestUser.setEmail(guestEmail);
    	    	guestUser.setPassword("");
    	    	
    	    	userRepo.addUser(guestUser);
    	    	
    	    	ctx.sessionAttribute("userId", guestUser.getId());
    	    }
    	});


        // Register controllers
        new TaskController().registerRoutes(app);
        new PlannerController().registerRoutes(app);
        new LoginController().registerRoutes(app);
    }
    
    private static int getHerokuPort() {
        String port = System.getenv("PORT");
        return port != null ? Integer.parseInt(port) : 7000;
    }
}


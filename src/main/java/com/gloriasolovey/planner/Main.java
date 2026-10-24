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
			Integer userId = ctx.sessionAttribute("userId");

			try {
				User guestUser = null;

				// Check whether the existing guest still exists
				if (userId != null) {
					guestUser = userRepo.getUserById(userId);
				}

				// Create a guest only if there is no valid user
				if (guestUser == null) {
					User newGuest = new User();
					newGuest.setEmail("GUEST-" + UUID.randomUUID());
					newGuest.setPassword("");

					userRepo.addUser(newGuest);

					ctx.sessionAttribute("userId", newGuest.getId());
				}

			} catch (Exception e) {
				// Log the actual database error in Railway
				e.printStackTrace();

				// Stop the request if the database is unavailable
				throw new io.javalin.http.InternalServerErrorResponse(
					"Unable to initialize guest session."
				);
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


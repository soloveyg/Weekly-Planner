package com.gloriasolovey.planner.controller;

import io.javalin.Javalin;

public class LoginController {

	public void registerRoutes(Javalin app) {
		app.get("/login", ctx -> {
		    ctx.redirect("/login.html");
		});
    }

}

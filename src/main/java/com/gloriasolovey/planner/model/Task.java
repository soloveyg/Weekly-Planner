package com.gloriasolovey.planner.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date; // Includes year, month, and day

    @Column(nullable = false)
    private LocalTime timeSlot; // Stores time in HH:mm format
    
    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String color;

    // Constructors
    public Task() {}  // Required by Hibernate

    public Task(String name, LocalDate date, LocalTime timeSlot, String color) {
        this.name = name;
        this.date = date;
        this.timeSlot = timeSlot;
        this.color = color;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime  getTimeSlot() { return timeSlot; }
    public void setTimeSlot(LocalTime  timeSlot) { this.timeSlot = timeSlot; }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}

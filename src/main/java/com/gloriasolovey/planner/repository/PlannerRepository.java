package com.gloriasolovey.planner.repository;


import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.gloriasolovey.planner.config.HibernateUtil;
import com.gloriasolovey.planner.model.Task;

public class PlannerRepository {

    // Add a new task
    public void addTask(Task task) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Task", Task.class).list();
        }
    }
    
    
    public List<Task> getTasksForUserInTimeFrame(int userId, LocalDate from, LocalDate to){
    	try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Task> query = session.createQuery(
                "FROM Task WHERE date BETWEEN :from AND :to", Task.class);
            query.setParameter("from", from);
            query.setParameter("to", to);
            return query.getResultList();
        }
    }

    // Find a task by ID
    public Task getTaskById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Task.class, id);
        }
    }

    // Update an existing task
    public void updateTask(int id, Task updatedTask) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Task existingTask = session.get(Task.class, id);
            if (existingTask != null) {
                // Ignore the id from the request body, as we use the URL ID
                existingTask.setName(updatedTask.getName());
                existingTask.setDate(updatedTask.getDate());
                existingTask.setTimeSlot(updatedTask.getTimeSlot());
                existingTask.setColor(updatedTask.getColor());

                session.merge(existingTask);
            }

            transaction.commit();
        }
    }



    // Delete a task by ID
    public void deleteTask(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Task task = session.get(Task.class, id);
            if (task != null) {
                session.remove(task);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}


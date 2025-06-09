package com.gloriasolovey.planner.repository;


import com.gloriasolovey.planner.config.HibernateUtil;
import com.gloriasolovey.planner.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserRepository {

    // Add a new user
    public void addUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Get all users
    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    // Find a user by ID
    public User getUserById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }
    
    // Find a user By Email
    public User getUserByEmail(String email) {
    	try(Session session = HibernateUtil.getSessionFactory().openSession()){
    		 Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
             query.setParameter("email", email);
             return query.uniqueResult();
    	}catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    // Delete a user by ID
    public void deleteUser(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}


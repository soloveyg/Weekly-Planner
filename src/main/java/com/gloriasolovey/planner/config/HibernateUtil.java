package com.gloriasolovey.planner.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Properties properties = new Properties();
            properties.load(HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties"));

            return new Configuration()
                    .setProperty("hibernate.connection.driver_class", properties.getProperty("hibernate.connection.driver_class"))
                    .setProperty("hibernate.connection.url", properties.getProperty("hibernate.connection.url"))
                    .setProperty("hibernate.connection.username", properties.getProperty("hibernate.connection.username"))
                    .setProperty("hibernate.connection.password", properties.getProperty("hibernate.connection.password"))
                    .setProperty("hibernate.dialect", properties.getProperty("hibernate.dialect"))
                    .setProperty("hibernate.show_sql", properties.getProperty("hibernate.show_sql"))
                    .setProperty("hibernate.format_sql", properties.getProperty("hibernate.format_sql"))
                    .setProperty("hibernate.hbm2ddl.auto", properties.getProperty("hibernate.hbm2ddl.auto"))
                    .addAnnotatedClass(com.gloriasolovey.planner.model.Task.class)
                    .addAnnotatedClass(com.gloriasolovey.planner.model.User.class)
                    .buildSessionFactory();
        } catch (Exception ex) {
            System.err.println("SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

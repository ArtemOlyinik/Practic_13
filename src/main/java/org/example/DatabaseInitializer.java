package org.example;

import org.example.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseInitializer {

  // Створюємо логер замість System.out
  private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);

    logger.info("Створення таблиць...");

    jdbcTemplate.execute(
        "CREATE TABLE IF NOT EXISTS departments ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT NOT NULL)");

    jdbcTemplate.execute(
        "CREATE TABLE IF NOT EXISTS professors ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "full_name TEXT NOT NULL, "
            + "email TEXT NOT NULL, "
            + "department_id INTEGER, "
            + "FOREIGN KEY(department_id) REFERENCES departments(id))");

    jdbcTemplate.execute(
        "CREATE TABLE IF NOT EXISTS courses ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "title TEXT NOT NULL, "
            + "credits INTEGER NOT NULL, "
            + "professor_id INTEGER, "
            + "FOREIGN KEY(professor_id) REFERENCES professors(id))");

    jdbcTemplate.execute(
        "CREATE TABLE IF NOT EXISTS students ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "first_name TEXT NOT NULL, "
            + "last_name TEXT NOT NULL, "
            + "age INTEGER NOT NULL, "
            + "course_id INTEGER, "
            + "FOREIGN KEY(course_id) REFERENCES courses(id))");

    logger.info("4 зв'язані таблиці успішно створено у файлі university.db!");
  }
}

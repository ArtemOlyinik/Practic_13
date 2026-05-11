package org.example;

import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.config.AppConfig;
import org.example.dao.StudentDao;
import org.example.model.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class Main extends Application {

  private TextArea consoleOutput;
  private StudentDao studentDao;
  private JdbcTemplate jdbcTemplate;

  @Override
  public void init() {
    // Ініціалізація Spring відбувається до запуску вікна
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    studentDao = context.getBean(StudentDao.class);
    jdbcTemplate = context.getBean(JdbcTemplate.class);

    // Додаємо тестові залежності, щоб не ламались зовнішні ключі
    jdbcTemplate.execute("INSERT OR IGNORE INTO departments (id, name) VALUES (1, 'Кафедра КН')");
    jdbcTemplate.execute(
        "INSERT OR IGNORE INTO professors (id, full_name, email, department_id) VALUES (1, 'Іван Іванов', 'ivan@test.com', 1)");
    jdbcTemplate.execute(
        "INSERT OR IGNORE INTO courses (id, title, credits, professor_id) VALUES (1, 'ООП', 5, 1)");
  }

  @Override
  public void start(Stage stage) {
    consoleOutput = new TextArea();
    consoleOutput.setEditable(false);
    consoleOutput.setPrefHeight(400);
    consoleOutput.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;");

    Button runDemoBtn = new Button("Запустити CRUD Демонстрацію");
    runDemoBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

    runDemoBtn.setOnAction(e -> runCrudDemo());

    VBox root = new VBox(15, runDemoBtn, consoleOutput);
    root.setPadding(new Insets(20));

    Scene scene = new Scene(root, 700, 500);
    stage.setScene(scene);
    stage.setTitle("Practice 13 - Spring JDBC + Validation + JavaFX");
    stage.show();

    print("Готово до тестування. Натисніть кнопку вище.");
  }

  private void runCrudDemo() {
    consoleOutput.clear();
    print("=== СТАРТ ДЕМОНСТРАЦІЇ CRUD ===");

    try {
      // 1. Тест валідації (негативний сценарій)
      print("\n[ТЕСТ ВАЛІДАЦІЇ]: Спроба зберегти студента віком 12 років...");
      Student invalidStudent = new Student(null, "Малий", "Школяр", 12, 1);
      studentDao.save(invalidStudent);
    } catch (IllegalArgumentException ex) {
      print("ОЧІКУВАНА ПОМИЛКА: " + ex.getMessage().trim());
    }

    // 2. persist (save) - успішний
    print("\n[CREATE]: Збереження коректних студентів...");
    Student s1 = new Student(null, "Артем", "Олійник", 20, 1);
    Student s2 = new Student(null, "Тарас", "Шевченко", 25, 1);
    studentDao.save(s1);
    studentDao.save(s2);
    print("Студентів збережено.");

    // 3. findAll
    print("\n[READ ALL]: Отримання списку всіх студентів...");
    List<Student> allStudents = studentDao.findAll();
    allStudents.forEach(s -> print(" - " + s));

    if (!allStudents.isEmpty()) {
      Student firstDbStudent = allStudents.get(0);

      // 4. update
      print("\n[UPDATE]: Оновлення прізвища першого студента на 'Змінено'...");
      firstDbStudent.setLastName("Змінено");
      studentDao.update(firstDbStudent);

      // 5. findById
      print("\n[READ BY ID]: Перевірка оновлення...");
      Student updated = studentDao.findById(firstDbStudent.getId());
      print("Оновлений запис: " + updated);

      // 6. delete
      print("\n[DELETE]: Видалення оновленого студента...");
      studentDao.delete(updated.getId());

      print("\n[READ ALL]: Список після видалення...");
      studentDao.findAll().forEach(s -> print(" - " + s));
    }

    print("\n=== ДЕМОНСТРАЦІЮ ЗАВЕРШЕНО ===");
  }

  private void print(String text) {
    Platform.runLater(() -> consoleOutput.appendText(text + "\n"));
  }

  public static void main(String[] args) {
    launch();
  }
}

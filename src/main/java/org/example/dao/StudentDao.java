package org.example.dao;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import org.example.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository // Кажемо Spring, що це компонент для роботи з БД
public class StudentDao {

  private final JdbcTemplate jdbcTemplate;
  private final Validator validator;

  @Autowired
  public StudentDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    // Ініціалізуємо Hibernate Validator
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    this.validator = factory.getValidator();
  }

  // Допоміжний метод для перевірки валідації перед запитом до БД (Пункт 5 на оцінку 5)
  private void validate(Student student) {
    Set<ConstraintViolation<Student>> violations = validator.validate(student);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder("Помилка валідації:\n");
      for (ConstraintViolation<Student> violation : violations) {
        sb.append("- ").append(violation.getMessage()).append("\n");
      }
      throw new IllegalArgumentException(sb.toString());
    }
  }

  // Мапер для перетворення рядків з БД у Java-об'єкт
  private final RowMapper<Student> rowMapper =
      new RowMapper<Student>() {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
          return new Student(
              rs.getInt("id"),
              rs.getString("first_name"),
              rs.getString("last_name"),
              rs.getInt("age"),
              rs.getInt("course_id"));
        }
      };

  // 1. persist (save)
  public void save(Student student) {
    validate(student); // Обов'язкова перевірка!
    String sql = "INSERT INTO students (first_name, last_name, age, course_id) VALUES (?, ?, ?, ?)";
    jdbcTemplate.update(
        sql,
        student.getFirstName(),
        student.getLastName(),
        student.getAge(),
        student.getCourseId());
  }

  // 2. update
  public void update(Student student) {
    validate(student); // Обов'язкова перевірка!
    String sql =
        "UPDATE students SET first_name = ?, last_name = ?, age = ?, course_id = ? WHERE id = ?";
    jdbcTemplate.update(
        sql,
        student.getFirstName(),
        student.getLastName(),
        student.getAge(),
        student.getCourseId(),
        student.getId());
  }

  // 3. findById
  public Student findById(int id) {
    String sql = "SELECT * FROM students WHERE id = ?";
    List<Student> students = jdbcTemplate.query(sql, rowMapper, id);
    return students.isEmpty() ? null : students.get(0);
  }

  // 4. findAll
  public List<Student> findAll() {
    String sql = "SELECT * FROM students";
    return jdbcTemplate.query(sql, rowMapper);
  }

  // 5. delete
  public void delete(int id) {
    String sql = "DELETE FROM students WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }
}

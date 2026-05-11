package org.example.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
  private Integer id;

  @NotBlank(message = "Ім'я не може бути порожнім")
  private String firstName;

  @NotBlank(message = "Прізвище не може бути порожнім")
  private String lastName;

  @NotNull(message = "Вік є обов'язковим")
  @Min(value = 16, message = "Студенту має бути мінімум 16 років")
  private Integer age;

  @NotNull(message = "ID курсу є обов'язковим")
  private Integer courseId;
}

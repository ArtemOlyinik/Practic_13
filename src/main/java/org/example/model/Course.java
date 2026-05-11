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
public class Course {
  private Integer id;

  @NotBlank(message = "Назва курсу не може бути порожньою")
  private String title;

  @NotNull(message = "Кількість кредитів є обов'язковою")
  @Min(value = 1, message = "Курс має містити мінімум 1 кредит")
  private Integer credits;

  @NotNull(message = "ID викладача є обов'язковим")
  private Integer professorId;
}

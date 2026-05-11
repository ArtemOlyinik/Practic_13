package org.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
  private Integer id;

  @NotBlank(message = "Назва кафедри не може бути порожньою")
  @Size(min = 2, max = 100, message = "Назва кафедри має містити від 2 до 100 символів")
  private String name;
}

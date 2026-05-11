package org.example.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professor {
  private Integer id;

  @NotBlank(message = "ПІБ викладача не може бути порожнім")
  private String fullName;

  @NotBlank(message = "Email не може бути порожнім")
  @Email(message = "Некоректний формат Email")
  private String email;

  @NotNull(message = "ID кафедри є обов'язковим")
  private Integer departmentId;
}

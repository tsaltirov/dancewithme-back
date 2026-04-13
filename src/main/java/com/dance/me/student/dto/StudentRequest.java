package com.dance.me.student.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String lastName;

    @Email(message = "Email no válido")
    private String email;

    private String phone;

    private LocalDate birthDate;

    @NotNull(message = "La escuela es obligatoria")
    private Long schoolId;
}

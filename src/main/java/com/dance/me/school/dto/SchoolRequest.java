package com.dance.me.school.dto;

import java.util.UUID;

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
public class SchoolRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String address;

    private String phone;

    @Email(message = "Email no válido")
    private String email;

    private String imageUrl;

    @NotNull(message = "El usuario propietario es obligatorio")
    private UUID userId;
}

package com.dance.me.enrollment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {

    @NotNull(message = "El alumno es obligatorio")
    private Long studentId;

    @NotNull(message = "El grupo es obligatorio")
    private Long groupId;

    private String notes;
}

package com.dance.me.coreografia.entity;

import java.math.BigDecimal;

import com.dance.me.student.entity.Student;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "escena_posiciones",
       uniqueConstraints = @UniqueConstraint(columnNames = {"escena_id", "alumno_id"}))
public class EscenaPosicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escena_id", nullable = false)
    private Escena escena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Student alumno;

    @Column(nullable = false, precision = 7, scale = 3)
    private BigDecimal x;

    @Column(nullable = false, precision = 7, scale = 3)
    private BigDecimal z;

    @Column(nullable = false, precision = 8, scale = 5)
    @Builder.Default
    private BigDecimal ry = BigDecimal.ZERO;
}

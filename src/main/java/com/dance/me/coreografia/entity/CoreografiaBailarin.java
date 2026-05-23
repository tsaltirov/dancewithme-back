package com.dance.me.coreografia.entity;

import com.dance.me.student.entity.Student;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coreografia_bailarines",
       uniqueConstraints = @UniqueConstraint(columnNames = {"coreografia_id", "alumno_id"}))
public class CoreografiaBailarin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coreografia_id", nullable = false)
    private Coreografia coreografia;

    // Al extraer al microservicio: reemplazar por plain Long alumnoId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Student alumno;

    // Color del bailarín en la vista (hex almacenado como entero, ej. 7167463 = #6D5327)
    @Column(nullable = false)
    private Integer color;

    @Column(nullable = false)
    private Short orden;
}

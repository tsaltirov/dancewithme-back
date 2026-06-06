package com.dance.me.coreografia.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.dance.me.school.entity.School;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coreografias")
public class Coreografia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK a School — cuando se extraiga al microservicio convertir a plain Long
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    // Slug público único — nunca cambia, usado para la URL compartible del frontend
    @Column(name = "public_slug", nullable = false, unique = true, updatable = false)
    @Builder.Default
    private UUID publicSlug = UUID.randomUUID();

    @Column(nullable = false, length = 200)
    @Builder.Default
    private String nombre = "Coreografía sin nombre";

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    @Column(name = "audio_nombre", length = 200)
    private String audioNombre;

    @Column(name = "stage_width", nullable = false, precision = 5, scale = 1)
    @Builder.Default
    private BigDecimal stageWidth = new BigDecimal("20.0");

    @Column(name = "stage_depth", nullable = false, precision = 5, scale = 1)
    @Builder.Default
    private BigDecimal stageDepth = new BigDecimal("13.0");

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

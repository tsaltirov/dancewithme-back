package com.dance.me.coreografia.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "escenas")
public class Escena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coreografia_id", nullable = false)
    private Coreografia coreografia;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Short orden;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal duracion;

    @Column(name = "hold_ratio", nullable = false, precision = 4, scale = 3)
    @Builder.Default
    private BigDecimal holdRatio = new BigDecimal("0.250");

    // Tipo libre: linear, smooth, ease-in, ease-out, ease-in-out
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String easing = "linear";

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "video_tipo", length = 10)
    private String videoTipo;

    @Column(name = "video_embed", length = 500)
    private String videoEmbed;
}

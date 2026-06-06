package com.dance.me.event.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_prices")
public class EventPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Tipo libre: "INSCRIPCION", "HOTEL", "VIAJE", "AUTOBUS", "CENA", etc.
    @Column(nullable = false, length = 50)
    private String type;

    // Descripción legible: "Hotel 2 noches Sevilla"
    @Column(length = 200)
    private String label;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal amount;

    // Si el alumno puede optar a no pagarlo
    @Column(nullable = false)
    @Builder.Default
    private Boolean optional = false;
}

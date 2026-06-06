package com.dance.me.costume.entity;

import java.time.LocalDate;

import com.dance.me.event.entity.EventParticipation;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_costumes")
public class EventCostume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id", nullable = false)
    private EventParticipation participation;

    // Referencia al catálogo — ya no se repite la descripción en cada evento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "costume_id", nullable = false)
    private Costume costume;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private CostumeStatus status = CostumeStatus.ENTREGADO;

    @Column(name = "delivery_date", nullable = false)
    @Builder.Default
    private LocalDate deliveryDate = LocalDate.now();

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;
}

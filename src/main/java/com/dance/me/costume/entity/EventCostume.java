package com.dance.me.costume.entity;

import java.time.LocalDate;

import com.dance.me.event.entity.EventParticipation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private CostumeStatus status = CostumeStatus.ENTREGADO;

    @Column(name = "delivery_date", nullable = false)
    @Builder.Default
    private LocalDate deliveryDate = LocalDate.now();

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "observations")
    private String observations;
}

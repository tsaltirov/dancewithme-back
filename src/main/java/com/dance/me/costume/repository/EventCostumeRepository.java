package com.dance.me.costume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.costume.entity.CostumeStatus;
import com.dance.me.costume.entity.EventCostume;

public interface EventCostumeRepository extends JpaRepository<EventCostume, Long> {

    List<EventCostume> findByParticipationId(Long participationId);

    List<EventCostume> findByStatusIn(List<CostumeStatus> statuses);

    List<EventCostume> findByCostumeId(Long costumeId);

    long countByCostumeIdAndStatusNot(Long costumeId, CostumeStatus status);
}

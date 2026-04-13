package com.dance.me.costume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dance.me.costume.entity.CostumeStatus;
import com.dance.me.costume.entity.EventCostume;

@Repository
public interface EventCostumeRepository extends JpaRepository<EventCostume, Long> {

    List<EventCostume> findByParticipationId(Long participationId);

    List<EventCostume> findByStatusIn(List<CostumeStatus> statuses);
}

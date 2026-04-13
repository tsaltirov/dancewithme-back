package com.dance.me.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dance.me.event.entity.EventParticipation;

@Repository
public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {

    List<EventParticipation> findByEventId(Long eventId);

    List<EventParticipation> findByStudentId(Long studentId);

    boolean existsByEventIdAndStudentId(Long eventId, Long studentId);
}

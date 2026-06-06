package com.dance.me.costume.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.costume.entity.EventBox;

public interface EventBoxRepository extends JpaRepository<EventBox, Long> {

    List<EventBox> findByEventId(Long eventId);

    Optional<EventBox> findByEventIdAndBoxId(Long eventId, Long boxId);

    boolean existsByEventIdAndBoxId(Long eventId, Long boxId);
}

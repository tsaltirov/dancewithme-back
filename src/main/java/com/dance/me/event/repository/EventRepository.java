package com.dance.me.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dance.me.event.entity.Event;
import com.dance.me.event.entity.EventStatus;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findBySchoolId(Long schoolId);

    List<Event> findBySchoolIdAndStatus(Long schoolId, EventStatus status);
}

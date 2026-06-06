package com.dance.me.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.event.entity.EventPrice;

public interface EventPriceRepository extends JpaRepository<EventPrice, Long> {

    List<EventPrice> findByEventId(Long eventId);

    void deleteByEventId(Long eventId);
}

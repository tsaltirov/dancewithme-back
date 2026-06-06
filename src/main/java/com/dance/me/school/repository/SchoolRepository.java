package com.dance.me.school.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dance.me.school.entity.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    List<School> findByUserId(UUID userId);

    List<School> findByActiveTrue();

    boolean existsByName(String name);
}

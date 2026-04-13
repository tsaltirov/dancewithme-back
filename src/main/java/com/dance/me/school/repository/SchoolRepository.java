package com.dance.me.school.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dance.me.school.entity.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    List<School> findByUserId(Long userId);

    List<School> findByActiveTrue();

    boolean existsByName(String name);
}

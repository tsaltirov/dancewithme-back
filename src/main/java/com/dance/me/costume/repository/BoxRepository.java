package com.dance.me.costume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.costume.entity.Box;

public interface BoxRepository extends JpaRepository<Box, Long> {

    List<Box> findBySchoolIdAndActiveTrue(Long schoolId);
}

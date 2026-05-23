package com.dance.me.costume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.costume.entity.Costume;

public interface CostumeRepository extends JpaRepository<Costume, Long> {

    List<Costume> findBySchoolIdAndActiveTrue(Long schoolId);

    List<Costume> findBySchoolIdAndActiveFalse(Long schoolId);

    List<Costume> findBySchoolId(Long schoolId);

    boolean existsByNameAndSchoolId(String name, Long schoolId);
}

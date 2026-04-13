package com.dance.me.group.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dance.me.group.entity.DanceGroup;

@Repository
public interface DanceGroupRepository extends JpaRepository<DanceGroup, Long> {

    List<DanceGroup> findBySchoolId(Long schoolId);

    List<DanceGroup> findBySchoolIdAndActiveTrue(Long schoolId);
}

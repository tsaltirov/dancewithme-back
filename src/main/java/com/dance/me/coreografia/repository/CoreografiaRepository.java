package com.dance.me.coreografia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.coreografia.entity.Coreografia;

public interface CoreografiaRepository extends JpaRepository<Coreografia, Long> {

    List<Coreografia> findBySchoolIdOrderByUpdatedAtDesc(Long schoolId);
}

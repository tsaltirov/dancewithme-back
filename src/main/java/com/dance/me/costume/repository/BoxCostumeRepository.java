package com.dance.me.costume.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.costume.entity.BoxCostume;

public interface BoxCostumeRepository extends JpaRepository<BoxCostume, Long> {

    List<BoxCostume> findByBoxId(Long boxId);

    Optional<BoxCostume> findByBoxIdAndCostumeId(Long boxId, Long costumeId);

    boolean existsByBoxIdAndCostumeId(Long boxId, Long costumeId);
}

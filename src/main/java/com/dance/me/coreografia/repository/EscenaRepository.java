package com.dance.me.coreografia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.coreografia.entity.Escena;

public interface EscenaRepository extends JpaRepository<Escena, Long> {

    List<Escena> findByCoreografiaIdOrderByOrden(Long coreografiaId);

    void deleteByCoreografiaId(Long coreografiaId);
}

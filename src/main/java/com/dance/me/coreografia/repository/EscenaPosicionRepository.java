package com.dance.me.coreografia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dance.me.coreografia.entity.EscenaPosicion;

public interface EscenaPosicionRepository extends JpaRepository<EscenaPosicion, Long> {

    List<EscenaPosicion> findByEscenaId(Long escenaId);

    @Modifying
    @Query("DELETE FROM EscenaPosicion ep WHERE ep.escena.coreografia.id = :coreografiaId")
    void deleteByCoreografiaId(Long coreografiaId);
}

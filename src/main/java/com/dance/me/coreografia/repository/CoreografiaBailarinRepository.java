package com.dance.me.coreografia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.coreografia.entity.CoreografiaBailarin;

public interface CoreografiaBailarinRepository extends JpaRepository<CoreografiaBailarin, Long> {

    List<CoreografiaBailarin> findByCoreografiaIdOrderByOrden(Long coreografiaId);

    void deleteByCoreografiaId(Long coreografiaId);
}

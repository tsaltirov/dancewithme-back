package com.dance.me.enrollment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dance.me.enrollment.entity.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByGroupId(Long groupId);

    boolean existsByStudentIdAndGroupId(Long studentId, Long groupId);
}

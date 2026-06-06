package com.dance.me.enrollment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dance.me.enrollment.entity.Enrollment;
import com.dance.me.enrollment.entity.EnrollmentStatus;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    List<Enrollment> findByGroupIdAndStatus(Long groupId, EnrollmentStatus status);

    Optional<Enrollment> findByStudentIdAndGroupId(Long studentId, Long groupId);

    boolean existsByStudentIdAndGroupIdAndStatus(Long studentId, Long groupId, EnrollmentStatus status);

    int countByGroupIdAndStatus(Long groupId, EnrollmentStatus status);
}

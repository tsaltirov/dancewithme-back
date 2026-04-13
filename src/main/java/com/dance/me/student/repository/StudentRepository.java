package com.dance.me.student.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dance.me.student.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findBySchoolId(Long schoolId);

    List<Student> findBySchoolIdAndActiveTrue(Long schoolId);

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);
}

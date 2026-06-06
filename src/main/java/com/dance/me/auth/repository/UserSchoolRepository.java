package com.dance.me.auth.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.auth.entity.UserSchool;
import com.dance.me.school.entity.School;
import com.dance.me.school.entity.SchoolRole;
import com.dance.me.user.entity.User;

public interface UserSchoolRepository extends JpaRepository<UserSchool, Long> {

    List<UserSchool> findByUserAndActiveTrue(User user);

    Optional<UserSchool> findByUserAndSchool(User user, School school);

    List<UserSchool> findBySchoolIdAndActiveTrue(Long schoolId);

    Optional<UserSchool> findByUserIdAndSchoolId(UUID userId, Long schoolId);

    boolean existsByUserIdAndSchoolIdAndSchoolRoleAndActiveTrue(
            UUID userId, Long schoolId, SchoolRole schoolRole);
}

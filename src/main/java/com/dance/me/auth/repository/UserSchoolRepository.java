package com.dance.me.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.auth.entity.UserSchool;
import com.dance.me.school.entity.School;
import com.dance.me.user.entity.User;

public interface UserSchoolRepository extends JpaRepository<UserSchool, Long> {

    List<UserSchool> findByUserAndActiveTrue(User user);

    Optional<UserSchool> findByUserAndSchool(User user, School school);
}

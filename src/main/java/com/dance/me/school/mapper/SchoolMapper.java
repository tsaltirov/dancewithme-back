package com.dance.me.school.mapper;

import org.springframework.stereotype.Component;

import com.dance.me.school.dto.SchoolRequest;
import com.dance.me.school.dto.SchoolResponse;
import com.dance.me.school.entity.School;
import com.dance.me.user.entity.User;

@Component
public class SchoolMapper {

    public SchoolResponse toResponse(School school) {
        return SchoolResponse.builder()
                .id(school.getId())
                .name(school.getName())
                .address(school.getAddress())
                .phone(school.getPhone())
                .email(school.getEmail())
                .userId(school.getUser().getId())
                .active(school.getActive())
                .createdAt(school.getCreatedAt())
                .build();
    }

    public School toEntity(SchoolRequest request, User user) {
        return School.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .user(user)
                .build();
    }
}

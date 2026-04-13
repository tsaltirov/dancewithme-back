package com.dance.me.group.mapper;

import org.springframework.stereotype.Component;

import com.dance.me.group.dto.GroupRequest;
import com.dance.me.group.dto.GroupResponse;
import com.dance.me.group.entity.DanceGroup;
import com.dance.me.school.entity.School;

@Component
public class GroupMapper {

    public GroupResponse toResponse(DanceGroup group) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .danceStyle(group.getDanceStyle())
                .level(group.getLevel())
                .maxCapacity(group.getMaxCapacity())
                .schedule(group.getSchedule())
                .schoolId(group.getSchool().getId())
                .schoolName(group.getSchool().getName())
                .active(group.getActive())
                .createdAt(group.getCreatedAt())
                .build();
    }

    public DanceGroup toEntity(GroupRequest request, School school) {
        return DanceGroup.builder()
                .name(request.getName())
                .danceStyle(request.getDanceStyle())
                .level(request.getLevel())
                .maxCapacity(request.getMaxCapacity())
                .schedule(request.getSchedule())
                .school(school)
                .build();
    }
}

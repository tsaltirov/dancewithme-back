package com.dance.me.group.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.group.dto.GroupRequest;
import com.dance.me.group.dto.GroupResponse;
import com.dance.me.group.entity.DanceGroup;
import com.dance.me.group.mapper.GroupMapper;
import com.dance.me.group.repository.DanceGroupRepository;
import com.dance.me.school.entity.School;
import com.dance.me.school.repository.SchoolRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final DanceGroupRepository groupRepository;
    private final SchoolRepository schoolRepository;
    private final GroupMapper groupMapper;

    public List<GroupResponse> findBySchoolId(Long schoolId) {
        return groupRepository.findBySchoolIdAndActiveTrue(schoolId).stream()
                .map(groupMapper::toResponse)
                .toList();
    }

    public GroupResponse findById(Long id) {
        return groupRepository.findById(id)
                .map(groupMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Group", id));
    }

    @Transactional
    public GroupResponse create(GroupRequest request) {
        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School", request.getSchoolId()));
        DanceGroup group = groupMapper.toEntity(request, school);
        return groupMapper.toResponse(groupRepository.save(group));
    }

    @Transactional
    public GroupResponse update(Long id, GroupRequest request) {
        DanceGroup group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group", id));
        group.setName(request.getName());
        group.setDanceStyle(request.getDanceStyle());
        group.setLevel(request.getLevel());
        group.setMaxCapacity(request.getMaxCapacity());
        group.setSchedule(request.getSchedule());
        return groupMapper.toResponse(groupRepository.save(group));
    }

    @Transactional
    public void deactivate(Long id) {
        DanceGroup group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group", id));
        group.setActive(false);
        groupRepository.save(group);
    }
}

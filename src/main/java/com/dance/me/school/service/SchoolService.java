package com.dance.me.school.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.school.dto.SchoolRequest;
import com.dance.me.school.dto.SchoolResponse;
import com.dance.me.school.entity.School;
import com.dance.me.school.mapper.SchoolMapper;
import com.dance.me.school.repository.SchoolRepository;
import com.dance.me.user.entity.User;
import com.dance.me.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final SchoolMapper schoolMapper;

    public List<SchoolResponse> findAll() {
        return schoolRepository.findByActiveTrue().stream()
                .map(schoolMapper::toResponse)
                .toList();
    }

    public SchoolResponse findById(Long id) {
        return schoolRepository.findById(id)
                .map(schoolMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("School", id));
    }

    public List<SchoolResponse> findByUserId(Long userId) {
        return schoolRepository.findByUserId(userId).stream()
                .map(schoolMapper::toResponse)
                .toList();
    }

    @Transactional
    public SchoolResponse create(SchoolRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));
        School school = schoolMapper.toEntity(request, user);
        return schoolMapper.toResponse(schoolRepository.save(school));
    }

    @Transactional
    public SchoolResponse update(Long id, SchoolRequest request) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School", id));
        school.setName(request.getName());
        school.setAddress(request.getAddress());
        school.setPhone(request.getPhone());
        school.setEmail(request.getEmail());
        return schoolMapper.toResponse(schoolRepository.save(school));
    }

    @Transactional
    public void deactivate(Long id) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School", id));
        school.setActive(false);
        schoolRepository.save(school);
    }
}

package com.dance.me.school.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.auth.entity.UserSchool;
import com.dance.me.auth.repository.UserSchoolRepository;
import com.dance.me.common.exception.BadRequestException;
import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.school.dto.AddMemberRequest;
import com.dance.me.school.dto.SchoolMemberResponse;
import com.dance.me.school.dto.SchoolRequest;
import com.dance.me.school.dto.SchoolResponse;
import com.dance.me.school.entity.School;
import com.dance.me.school.entity.SchoolRole;
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
    private final UserSchoolRepository userSchoolRepository;
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

    public List<SchoolResponse> findByUserId(UUID userId) {
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

    public List<SchoolMemberResponse> findMembers(Long schoolId) {
        return userSchoolRepository.findBySchoolIdAndActiveTrue(schoolId).stream()
                .map(this::toMemberResponse)
                .toList();
    }

    @Transactional
    public SchoolMemberResponse addMember(Long schoolId, UUID requesterId, AddMemberRequest request) {
        checkOwner(schoolId, requesterId);

        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("School", schoolId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));

        if (request.getRole() == SchoolRole.OWNER) {
            throw new BadRequestException("No se puede asignar el rol OWNER", "INVALID_ROLE");
        }

        userSchoolRepository.findByUserIdAndSchoolId(user.getId(), schoolId).ifPresent(existing -> {
            throw new BadRequestException(
                    "El usuario ya pertenece a esta escuela", "MEMBER_ALREADY_EXISTS");
        });

        UserSchool membership = UserSchool.builder()
                .user(user)
                .school(school)
                .schoolRole(request.getRole())
                .build();

        return toMemberResponse(userSchoolRepository.save(membership));
    }

    @Transactional
    public void removeMember(Long schoolId, UUID requesterId, UUID targetUserId) {
        checkOwner(schoolId, requesterId);

        if (requesterId.equals(targetUserId)) {
            throw new BadRequestException("El OWNER no puede eliminarse a sí mismo", "CANNOT_REMOVE_OWNER");
        }

        UserSchool membership = userSchoolRepository
                .findByUserIdAndSchoolId(targetUserId, schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", targetUserId));

        userSchoolRepository.delete(membership);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private void checkOwner(Long schoolId, UUID userId) {
        boolean isOwner = userSchoolRepository
                .existsByUserIdAndSchoolIdAndSchoolRoleAndActiveTrue(userId, schoolId, SchoolRole.OWNER);
        if (!isOwner) {
            throw new BadRequestException("Solo el OWNER puede gestionar los miembros de la escuela", "FORBIDDEN_NOT_OWNER");
        }
    }

    private SchoolMemberResponse toMemberResponse(UserSchool us) {
        User u = us.getUser();
        return SchoolMemberResponse.builder()
                .userId(u.getId())
                .name(u.getName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .imageUrl(u.getImageUrl())
                .role(us.getSchoolRole())
                .active(us.getActive())
                .build();
    }
}

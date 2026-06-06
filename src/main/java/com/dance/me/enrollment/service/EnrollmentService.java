package com.dance.me.enrollment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.common.exception.BadRequestException;
import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.enrollment.dto.EnrollmentRequest;
import com.dance.me.enrollment.dto.EnrollmentResponse;
import com.dance.me.enrollment.entity.Enrollment;
import com.dance.me.enrollment.entity.EnrollmentStatus;
import com.dance.me.enrollment.mapper.EnrollmentMapper;
import com.dance.me.enrollment.repository.EnrollmentRepository;
import com.dance.me.group.entity.DanceGroup;
import com.dance.me.group.repository.DanceGroupRepository;
import com.dance.me.student.entity.Student;
import com.dance.me.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final DanceGroupRepository groupRepository;
    private final EnrollmentMapper enrollmentMapper;

    public List<EnrollmentResponse> findByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.ACTIVA).stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    public List<EnrollmentResponse> findByGroupId(Long groupId) {
        return enrollmentRepository.findByGroupIdAndStatus(groupId, EnrollmentStatus.ACTIVA).stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Transactional
    public EnrollmentResponse create(EnrollmentRequest request) {
        if (enrollmentRepository.existsByStudentIdAndGroupIdAndStatus(
                request.getStudentId(), request.getGroupId(), EnrollmentStatus.ACTIVA)) {
            throw new BadRequestException("El alumno ya está inscrito en este grupo");
        }
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.getStudentId()));
        DanceGroup group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group", request.getGroupId()));
        if (group.getMaxCapacity() != null) {
            int active = enrollmentRepository.countByGroupIdAndStatus(group.getId(), EnrollmentStatus.ACTIVA);
            if (active >= group.getMaxCapacity()) {
                throw new BadRequestException("El grupo ha alcanzado su capacidad máxima de " + group.getMaxCapacity() + " alumnos");
            }
        }
        // Si existe un registro previo en BAJA/SUSPENDIDA, reactivarlo en lugar de insertar
        Optional<Enrollment> existing = enrollmentRepository.findByStudentIdAndGroupId(
                request.getStudentId(), request.getGroupId());
        if (existing.isPresent()) {
            Enrollment enrollment = existing.get();
            enrollment.setStatus(EnrollmentStatus.ACTIVA);
            enrollment.setEnrollmentDate(LocalDate.now());
            enrollment.setNotes(request.getNotes());
            return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
        }
        Enrollment enrollment = enrollmentMapper.toEntity(student, group, request.getNotes());
        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Transactional
    public EnrollmentResponse withdraw(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
        enrollment.setStatus(EnrollmentStatus.BAJA);
        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }
}

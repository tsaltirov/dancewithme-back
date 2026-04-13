package com.dance.me.student.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.common.exception.BadRequestException;
import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.school.entity.School;
import com.dance.me.school.repository.SchoolRepository;
import com.dance.me.student.dto.StudentRequest;
import com.dance.me.student.dto.StudentResponse;
import com.dance.me.student.entity.Student;
import com.dance.me.student.mapper.StudentMapper;
import com.dance.me.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final StudentMapper studentMapper;

    public List<StudentResponse> findBySchoolId(Long schoolId) {
        return studentRepository.findBySchoolIdAndActiveTrue(schoolId).stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    public StudentResponse findById(Long id) {
        return studentRepository.findById(id)
                .map(studentMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }

    @Transactional
    public StudentResponse create(StudentRequest request) {
        if (request.getEmail() != null && studentRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Ya existe un alumno con ese email");
        }
        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School", request.getSchoolId()));
        Student student = studentMapper.toEntity(request, school);
        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
        student.setName(request.getName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setBirthDate(request.getBirthDate());
        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Transactional
    public void deactivate(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
        student.setActive(false);
        studentRepository.save(student);
    }
}

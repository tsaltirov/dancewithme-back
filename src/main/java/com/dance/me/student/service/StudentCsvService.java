package com.dance.me.student.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dance.me.common.exception.BadRequestException;
import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.school.entity.School;
import com.dance.me.school.repository.SchoolRepository;
import com.dance.me.student.dto.CsvImportResponse;
import com.dance.me.student.dto.CsvRowError;
import com.dance.me.student.entity.Student;
import com.dance.me.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentCsvService {

    private static final String[] HEADERS = { "name", "lastName", "email", "phone", "birthDate" };
    private static final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    @Transactional
    public CsvImportResponse importStudents(MultipartFile file, Long schoolId) {
        if (file.isEmpty()) {
            throw new BadRequestException("El archivo CSV está vacío");
        }
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            throw new BadRequestException("El archivo debe tener extensión .csv");
        }

        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("School", schoolId));

        List<CsvRowError> errors = new ArrayList<>();
        List<Student> toSave = new ArrayList<>();
        Set<String> emailsInBatch = new HashSet<>();
        int total = 0;

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();

        try (
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            CSVParser parser = format.parse(reader)
        ) {
            for (CSVRecord record : parser) {
                total++;
                int lineNum = total + 1; // +1 for header line

                String name      = record.get("name");
                String lastName  = record.get("lastName");
                String email     = record.get("email");
                String phone     = record.get("phone");
                String birthDate = record.get("birthDate");
                String rawData   = record.toString();

                if (name == null || name.isBlank()) {
                    errors.add(new CsvRowError(lineNum, "El nombre es obligatorio", rawData));
                    continue;
                }
                if (lastName == null || lastName.isBlank()) {
                    errors.add(new CsvRowError(lineNum, "Los apellidos son obligatorios", rawData));
                    continue;
                }

                String normalizedEmail = (email == null || email.isBlank()) ? null : email.toLowerCase();
                if (normalizedEmail != null) {
                    if (!normalizedEmail.matches(EMAIL_REGEX)) {
                        errors.add(new CsvRowError(lineNum, "Email no válido: " + normalizedEmail, rawData));
                        continue;
                    }
                    if (emailsInBatch.contains(normalizedEmail)) {
                        errors.add(new CsvRowError(lineNum, "Email duplicado en el CSV: " + normalizedEmail, rawData));
                        continue;
                    }
                    if (studentRepository.existsByEmail(normalizedEmail)) {
                        errors.add(new CsvRowError(lineNum, "Ya existe un alumno con ese email: " + normalizedEmail, rawData));
                        continue;
                    }
                    emailsInBatch.add(normalizedEmail);
                }

                LocalDate parsedBirthDate = null;
                if (birthDate != null && !birthDate.isBlank()) {
                    try {
                        parsedBirthDate = LocalDate.parse(birthDate);
                    } catch (DateTimeParseException e) {
                        errors.add(new CsvRowError(lineNum, "Fecha inválida (usa yyyy-MM-dd): " + birthDate, rawData));
                        continue;
                    }
                }

                toSave.add(Student.builder()
                        .name(name)
                        .lastName(lastName)
                        .email(normalizedEmail)
                        .phone(phone == null || phone.isBlank() ? null : phone)
                        .birthDate(parsedBirthDate)
                        .school(school)
                        .build());
            }
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Error al procesar el CSV: " + e.getMessage());
        }

        studentRepository.saveAll(toSave);

        return CsvImportResponse.builder()
                .total(total)
                .created(toSave.size())
                .failed(errors.size())
                .errors(errors)
                .build();
    }
}

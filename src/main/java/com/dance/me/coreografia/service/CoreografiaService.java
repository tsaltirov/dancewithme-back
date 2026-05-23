package com.dance.me.coreografia.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.coreografia.dto.BailarinDto;
import com.dance.me.coreografia.dto.CoreografiaListResponse;
import com.dance.me.coreografia.dto.CoreografiaRequest;
import com.dance.me.coreografia.dto.CoreografiaResponse;
import com.dance.me.coreografia.dto.EscenaDto;
import com.dance.me.coreografia.dto.EscenaPosicionDto;
import com.dance.me.coreografia.entity.Coreografia;
import com.dance.me.coreografia.entity.CoreografiaBailarin;
import com.dance.me.coreografia.entity.Escena;
import com.dance.me.coreografia.entity.EscenaPosicion;
import com.dance.me.coreografia.repository.CoreografiaBailarinRepository;
import com.dance.me.coreografia.repository.CoreografiaRepository;
import com.dance.me.coreografia.repository.EscenaPosicionRepository;
import com.dance.me.coreografia.repository.EscenaRepository;
import com.dance.me.config.AppProperties;
import com.dance.me.school.entity.School;
import com.dance.me.school.repository.SchoolRepository;
import com.dance.me.student.entity.Student;
import com.dance.me.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoreografiaService {

    private final CoreografiaRepository coreografiaRepository;
    private final CoreografiaBailarinRepository bailarinRepository;
    private final EscenaRepository escenaRepository;
    private final EscenaPosicionRepository posicionRepository;
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    private final AppProperties appProperties;

    // ── Listado ligero ────────────────────────────────────────────────────────

    public List<CoreografiaListResponse> findBySchool(Long schoolId) {
        return coreografiaRepository.findBySchoolIdOrderByUpdatedAtDesc(schoolId).stream()
                .map(c -> CoreografiaListResponse.builder()
                        .id(c.getId())
                        .schoolId(c.getSchool().getId())
                        .publicUrl(buildPublicUrl(c))
                        .nombre(c.getNombre())
                        .audioNombre(c.getAudioNombre())
                        .totalBailarines((int) bailarinRepository.findByCoreografiaIdOrderByOrden(c.getId()).size())
                        .totalEscenas((int) escenaRepository.findByCoreografiaIdOrderByOrden(c.getId()).size())
                        .updatedAt(c.getUpdatedAt())
                        .build())
                .toList();
    }

    // ── Documento completo ────────────────────────────────────────────────────

    public CoreografiaResponse findById(Long id) {
        Coreografia c = get(id);
        return toFullResponse(c);
    }

    // ── Crear ─────────────────────────────────────────────────────────────────

    @Transactional
    public CoreografiaResponse create(CoreografiaRequest request) {
        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School", request.getSchoolId()));

        Coreografia coreografia = Coreografia.builder()
                .school(school)
                .nombre(request.getNombre() != null ? request.getNombre() : "Coreografía sin nombre")
                .audioUrl(request.getAudioUrl())
                .audioNombre(request.getAudioNombre())
                .stageWidth(request.getStageWidth() != null ? request.getStageWidth()
                        : new java.math.BigDecimal("20.0"))
                .stageDepth(request.getStageDepth() != null ? request.getStageDepth()
                        : new java.math.BigDecimal("13.0"))
                .build();

        coreografia = coreografiaRepository.save(coreografia);
        saveChildren(coreografia, request);
        return toFullResponse(coreografia);
    }

    // ── Guardar documento completo (reemplaza todo en una transacción) ────────

    @Transactional
    public CoreografiaResponse save(Long id, CoreografiaRequest request) {
        Coreografia coreografia = get(id);

        // Actualizar campos de la coreografía
        if (request.getNombre() != null) coreografia.setNombre(request.getNombre());
        if (request.getAudioUrl() != null) coreografia.setAudioUrl(request.getAudioUrl());
        if (request.getAudioNombre() != null) coreografia.setAudioNombre(request.getAudioNombre());
        if (request.getStageWidth() != null) coreografia.setStageWidth(request.getStageWidth());
        if (request.getStageDepth() != null) coreografia.setStageDepth(request.getStageDepth());
        coreografia = coreografiaRepository.save(coreografia);

        // Borrar todo lo anterior y reemplazar
        posicionRepository.deleteByCoreografiaId(id);
        escenaRepository.deleteByCoreografiaId(id);
        bailarinRepository.deleteByCoreografiaId(id);

        saveChildren(coreografia, request);
        return toFullResponse(coreografia);
    }

    // ── Borrar (CASCADE en BD elimina hijos automáticamente) ──────────────────

    @Transactional
    public void delete(Long id) {
        if (!coreografiaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coreografia", id);
        }
        posicionRepository.deleteByCoreografiaId(id);
        escenaRepository.deleteByCoreografiaId(id);
        bailarinRepository.deleteByCoreografiaId(id);
        coreografiaRepository.deleteById(id);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private void saveChildren(Coreografia coreografia, CoreografiaRequest request) {
        // Bailarines
        for (BailarinDto dto : request.getBailarines()) {
            Student alumno = studentRepository.findById(dto.getAlumnoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", dto.getAlumnoId()));
            bailarinRepository.save(CoreografiaBailarin.builder()
                    .coreografia(coreografia)
                    .alumno(alumno)
                    .color(dto.getColor())
                    .orden(dto.getOrden())
                    .build());
        }

        // Escenas y posiciones
        for (EscenaDto escenaDto : request.getEscenas()) {
            Escena escena = escenaRepository.save(Escena.builder()
                    .coreografia(coreografia)
                    .nombre(escenaDto.getNombre())
                    .orden(escenaDto.getOrden())
                    .duracion(escenaDto.getDuracion())
                    .holdRatio(escenaDto.getHoldRatio() != null
                            ? escenaDto.getHoldRatio() : new java.math.BigDecimal("0.250"))
                    .easing(escenaDto.getEasing() != null ? escenaDto.getEasing() : "linear")
                    .videoUrl(escenaDto.getVideoUrl())
                    .videoTipo(escenaDto.getVideoTipo())
                    .videoEmbed(escenaDto.getVideoEmbed())
                    .build());

            for (EscenaPosicionDto posDto : escenaDto.getPosiciones()) {
                Student alumno = studentRepository.findById(posDto.getAlumnoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Student", posDto.getAlumnoId()));
                posicionRepository.save(EscenaPosicion.builder()
                        .escena(escena)
                        .alumno(alumno)
                        .x(posDto.getX())
                        .z(posDto.getZ())
                        .ry(posDto.getRy() != null ? posDto.getRy() : java.math.BigDecimal.ZERO)
                        .build());
            }
        }
    }

    private CoreografiaResponse toFullResponse(Coreografia c) {
        List<BailarinDto> bailarines = bailarinRepository
                .findByCoreografiaIdOrderByOrden(c.getId()).stream()
                .map(b -> {
                    BailarinDto dto = new BailarinDto();
                    dto.setAlumnoId(b.getAlumno().getId());
                    dto.setColor(b.getColor());
                    dto.setOrden(b.getOrden());
                    return dto;
                }).toList();

        List<EscenaDto> escenas = escenaRepository
                .findByCoreografiaIdOrderByOrden(c.getId()).stream()
                .map(e -> {
                    EscenaDto dto = new EscenaDto();
                    dto.setId(e.getId());
                    dto.setNombre(e.getNombre());
                    dto.setOrden(e.getOrden());
                    dto.setDuracion(e.getDuracion());
                    dto.setHoldRatio(e.getHoldRatio());
                    dto.setEasing(e.getEasing());
                    dto.setVideoUrl(e.getVideoUrl());
                    dto.setVideoTipo(e.getVideoTipo());
                    dto.setVideoEmbed(e.getVideoEmbed());
                    dto.setPosiciones(posicionRepository.findByEscenaId(e.getId()).stream()
                            .map(p -> {
                                EscenaPosicionDto pDto = new EscenaPosicionDto();
                                pDto.setAlumnoId(p.getAlumno().getId());
                                pDto.setX(p.getX());
                                pDto.setZ(p.getZ());
                                pDto.setRy(p.getRy());
                                return pDto;
                            }).toList());
                    return dto;
                }).toList();

        return CoreografiaResponse.builder()
                .id(c.getId())
                .schoolId(c.getSchool().getId())
                .publicUrl(buildPublicUrl(c))
                .nombre(c.getNombre())
                .audioUrl(c.getAudioUrl())
                .audioNombre(c.getAudioNombre())
                .stageWidth(c.getStageWidth())
                .stageDepth(c.getStageDepth())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .bailarines(bailarines)
                .escenas(escenas)
                .build();
    }

    private String buildPublicUrl(Coreografia c) {
        return appProperties.getFrontendUrl()
                + "/coreos/" + c.getSchool().getId()
                + "/" + c.getPublicSlug();
    }

    private Coreografia get(Long id) {
        return coreografiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coreografia", id));
    }
}

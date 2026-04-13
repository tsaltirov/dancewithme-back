package com.dance.me.costume.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.costume.dto.CostumeRequest;
import com.dance.me.costume.dto.CostumeResponse;
import com.dance.me.costume.entity.CostumeStatus;
import com.dance.me.costume.entity.EventCostume;
import com.dance.me.costume.mapper.CostumeMapper;
import com.dance.me.costume.repository.EventCostumeRepository;
import com.dance.me.event.entity.EventParticipation;
import com.dance.me.event.repository.EventParticipationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CostumeService {

    private final EventCostumeRepository costumeRepository;
    private final EventParticipationRepository participationRepository;
    private final CostumeMapper costumeMapper;

    public List<CostumeResponse> findByParticipationId(Long participationId) {
        return costumeRepository.findByParticipationId(participationId).stream()
                .map(costumeMapper::toResponse)
                .toList();
    }

    public List<CostumeResponse> findPendingReturn() {
        List<CostumeStatus> statuses = List.of(CostumeStatus.ENTREGADO, CostumeStatus.PENDIENTE_DEVOLUCION);
        return costumeRepository.findByStatusIn(statuses).stream()
                .map(costumeMapper::toResponse)
                .toList();
    }

    @Transactional
    public CostumeResponse create(CostumeRequest request) {
        EventParticipation participation = participationRepository.findById(request.getParticipationId())
                .orElseThrow(() -> new ResourceNotFoundException("Participation", request.getParticipationId()));
        EventCostume costume = costumeMapper.toEntity(
                participation, request.getDescription(), request.getObservations());
        return costumeMapper.toResponse(costumeRepository.save(costume));
    }

    @Transactional
    public CostumeResponse markAsReturned(Long id) {
        EventCostume costume = costumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Costume", id));
        costume.setStatus(CostumeStatus.DEVUELTO);
        costume.setReturnDate(LocalDate.now());
        return costumeMapper.toResponse(costumeRepository.save(costume));
    }
}

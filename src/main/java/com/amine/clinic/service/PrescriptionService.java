package com.amine.clinic.service;

import com.amine.clinic.dto.PrescriptionRequestDTO;
import com.amine.clinic.dto.PrescriptionResponseDTO;

import java.util.List;

public interface PrescriptionService {

    PrescriptionResponseDTO create(PrescriptionRequestDTO request);

    PrescriptionResponseDTO findById(Long id);

    List<PrescriptionResponseDTO> findAll();

    PrescriptionResponseDTO update(Long id, PrescriptionRequestDTO request);

    void delete(Long id);
}

package com.amine.clinic.service;

import com.amine.clinic.dto.PatientRequestDTO;
import com.amine.clinic.dto.PatientResponseDTO;

import java.util.List;

public interface PatientService {

    PatientResponseDTO create(PatientRequestDTO request);

    PatientResponseDTO findById(Long id);

    List<PatientResponseDTO> findAll();

    PatientResponseDTO update(Long id, PatientRequestDTO request);

    void delete(Long id);
}

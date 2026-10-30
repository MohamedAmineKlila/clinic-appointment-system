package com.amine.clinic.service;

import com.amine.clinic.dto.MedicalRecordRequestDTO;
import com.amine.clinic.dto.MedicalRecordResponseDTO;

import java.util.List;

public interface MedicalRecordService {

    MedicalRecordResponseDTO create(MedicalRecordRequestDTO request);

    MedicalRecordResponseDTO findById(Long id);

    List<MedicalRecordResponseDTO> findAll();

    MedicalRecordResponseDTO update(Long id, MedicalRecordRequestDTO request);

    void delete(Long id);
}

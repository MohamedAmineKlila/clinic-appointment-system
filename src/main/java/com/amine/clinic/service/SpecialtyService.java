package com.amine.clinic.service;

import com.amine.clinic.dto.SpecialtyRequestDTO;
import com.amine.clinic.dto.SpecialtyResponseDTO;

import java.util.List;

public interface SpecialtyService {

    SpecialtyResponseDTO create(SpecialtyRequestDTO request);

    SpecialtyResponseDTO findById(Long id);

    List<SpecialtyResponseDTO> findAll();

    SpecialtyResponseDTO update(Long id, SpecialtyRequestDTO request);

    void delete(Long id);
}

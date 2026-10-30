package com.amine.clinic.service;

import com.amine.clinic.dto.DoctorRequestDTO;
import com.amine.clinic.dto.DoctorResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {

    DoctorResponseDTO create(DoctorRequestDTO request);

    DoctorResponseDTO findById(Long id);

    List<DoctorResponseDTO> findAll();

    DoctorResponseDTO update(Long id, DoctorRequestDTO request);

    void delete(Long id);

    List<DoctorResponseDTO> findBySpecialty(Long specialtyId);

    List<DoctorResponseDTO> findAvailable(LocalDate date);
}

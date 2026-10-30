package com.amine.clinic.service;

import com.amine.clinic.dto.AppointmentRequestDTO;
import com.amine.clinic.dto.AppointmentResponseDTO;
import com.amine.clinic.model.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO create(AppointmentRequestDTO request);

    AppointmentResponseDTO findById(Long id);

    List<AppointmentResponseDTO> findAll();

    AppointmentResponseDTO update(Long id, AppointmentRequestDTO request);

    void delete(Long id);

    List<AppointmentResponseDTO> findByDoctorAndDate(Long doctorId, LocalDate date);

    List<AppointmentResponseDTO> findByPatient(Long patientId);

    long countByStatus(AppointmentStatus status);

    List<AppointmentResponseDTO> findByStatusAndDateRange(AppointmentStatus status, LocalDate start, LocalDate end);
}

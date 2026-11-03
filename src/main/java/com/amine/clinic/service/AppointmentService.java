package com.amine.clinic.service;

import com.amine.clinic.model.Appointment;
import com.amine.clinic.model.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    Appointment create(Appointment appointment);

    Appointment findById(Long id);

    List<Appointment> findAll();

    Appointment update(Long id, Appointment appointment);

    void delete(Long id);

    List<Appointment> findByDoctorAndDate(Long doctorId, LocalDate date);

    List<Appointment> findByPatient(Long patientId);

    long countByStatus(AppointmentStatus status);

    List<Appointment> findByStatusAndDateRange(AppointmentStatus status, LocalDate start, LocalDate end);
}

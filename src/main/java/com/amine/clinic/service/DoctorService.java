package com.amine.clinic.service;

import com.amine.clinic.model.Doctor;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {

    Doctor create(Doctor doctor);

    Doctor findById(Long id);

    List<Doctor> findAll();

    Doctor update(Long id, Doctor doctor);

    void delete(Long id);

    List<Doctor> findBySpecialty(Long specialtyId);

    List<Doctor> findAvailable(LocalDate date);
}

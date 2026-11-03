package com.amine.clinic.service;

import com.amine.clinic.model.Patient;

import java.util.List;

public interface PatientService {

    Patient create(Patient patient);

    Patient findById(Long id);

    List<Patient> findAll();

    Patient update(Long id, Patient patient);

    void delete(Long id);
}

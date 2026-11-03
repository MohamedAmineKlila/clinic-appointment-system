package com.amine.clinic.service;

import com.amine.clinic.model.Prescription;

import java.util.List;

public interface PrescriptionService {

    Prescription create(Prescription prescription);

    Prescription findById(Long id);

    List<Prescription> findAll();

    Prescription update(Long id, Prescription prescription);

    void delete(Long id);
}

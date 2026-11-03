package com.amine.clinic.service;

import com.amine.clinic.model.Specialty;

import java.util.List;

public interface SpecialtyService {

    Specialty create(Specialty specialty);

    Specialty findById(Long id);

    List<Specialty> findAll();

    Specialty update(Long id, Specialty specialty);

    void delete(Long id);
}

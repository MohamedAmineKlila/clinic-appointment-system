package com.amine.clinic.service;

import com.amine.clinic.exception.DuplicateResourceException;
import com.amine.clinic.exception.ResourceNotFoundException;
import com.amine.clinic.model.Specialty;
import com.amine.clinic.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public Specialty create(Specialty specialty) {
        specialtyRepository.findByName(specialty.getName()).ifPresent(existing -> {
            throw new DuplicateResourceException("Specialty with name '" + specialty.getName() + "' already exists");
        });
        return specialtyRepository.save(specialty);
    }

    @Override
    @Transactional(readOnly = true)
    public Specialty findById(Long id) {
        return getOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Specialty> findAll() {
        return specialtyRepository.findAll();
    }

    @Override
    public Specialty update(Long id, Specialty request) {
        Specialty specialty = getOrThrow(id);
        specialtyRepository.findByName(request.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("Specialty with name '" + request.getName() + "' already exists");
            }
        });
        specialty.setName(request.getName());
        specialty.setDescription(request.getDescription());
        return specialtyRepository.save(specialty);
    }

    @Override
    public void delete(Long id) {
        Specialty specialty = getOrThrow(id);
        specialtyRepository.delete(specialty);
    }

    private Specialty getOrThrow(Long id) {
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialty not found with id: " + id));
    }
}

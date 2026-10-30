package com.amine.clinic.service;

import com.amine.clinic.dto.PatientRequestDTO;
import com.amine.clinic.dto.PatientResponseDTO;
import com.amine.clinic.exception.DuplicateResourceException;
import com.amine.clinic.exception.ResourceNotFoundException;
import com.amine.clinic.model.Patient;
import com.amine.clinic.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientResponseDTO create(PatientRequestDTO request) {
        patientRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
            throw new DuplicateResourceException("Patient with email '" + request.getEmail() + "' already exists");
        });
        Patient patient = new Patient();
        mapRequestToEntity(request, patient);
        return toResponse(patientRepository.save(patient));
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDTO findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> findAll() {
        return patientRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public PatientResponseDTO update(Long id, PatientRequestDTO request) {
        Patient patient = getOrThrow(id);
        patientRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("Patient with email '" + request.getEmail() + "' already exists");
            }
        });
        mapRequestToEntity(request, patient);
        return toResponse(patientRepository.save(patient));
    }

    @Override
    public void delete(Long id) {
        Patient patient = getOrThrow(id);
        patientRepository.delete(patient);
    }

    private Patient getOrThrow(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    private void mapRequestToEntity(PatientRequestDTO request, Patient patient) {
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
    }

    private PatientResponseDTO toResponse(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getDateOfBirth()
        );
    }
}

package com.amine.clinic.service;

import com.amine.clinic.dto.MedicalRecordRequestDTO;
import com.amine.clinic.dto.MedicalRecordResponseDTO;
import com.amine.clinic.exception.DuplicateResourceException;
import com.amine.clinic.exception.ResourceNotFoundException;
import com.amine.clinic.model.MedicalRecord;
import com.amine.clinic.model.Patient;
import com.amine.clinic.repository.MedicalRecordRepository;
import com.amine.clinic.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public MedicalRecordResponseDTO create(MedicalRecordRequestDTO request) {
        Patient patient = getPatient(request.getPatientId());
        medicalRecordRepository.findByPatientId(patient.getId()).ifPresent(existing -> {
            throw new DuplicateResourceException("Medical record already exists for patient id: " + patient.getId());
        });
        MedicalRecord record = new MedicalRecord();
        mapRequestToEntity(request, record, patient);
        return toResponse(medicalRecordRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordResponseDTO findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordResponseDTO> findAll() {
        return medicalRecordRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public MedicalRecordResponseDTO update(Long id, MedicalRecordRequestDTO request) {
        MedicalRecord record = getOrThrow(id);
        Patient patient = getPatient(request.getPatientId());
        medicalRecordRepository.findByPatientId(patient.getId()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("Medical record already exists for patient id: " + patient.getId());
            }
        });
        mapRequestToEntity(request, record, patient);
        return toResponse(medicalRecordRepository.save(record));
    }

    @Override
    public void delete(Long id) {
        MedicalRecord record = getOrThrow(id);
        medicalRecordRepository.delete(record);
    }

    private Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
    }

    private MedicalRecord getOrThrow(Long id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found with id: " + id));
    }

    private void mapRequestToEntity(MedicalRecordRequestDTO request, MedicalRecord record, Patient patient) {
        record.setBloodType(request.getBloodType());
        record.setAllergies(request.getAllergies());
        record.setChronicConditions(request.getChronicConditions());
        record.setPatient(patient);
    }

    private MedicalRecordResponseDTO toResponse(MedicalRecord record) {
        return new MedicalRecordResponseDTO(
                record.getId(),
                record.getBloodType(),
                record.getAllergies(),
                record.getChronicConditions(),
                record.getPatient() != null ? record.getPatient().getId() : null
        );
    }
}

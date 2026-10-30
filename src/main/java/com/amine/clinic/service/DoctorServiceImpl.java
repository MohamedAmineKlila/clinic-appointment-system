package com.amine.clinic.service;

import com.amine.clinic.dto.DoctorRequestDTO;
import com.amine.clinic.dto.DoctorResponseDTO;
import com.amine.clinic.exception.DuplicateResourceException;
import com.amine.clinic.exception.ResourceNotFoundException;
import com.amine.clinic.model.Doctor;
import com.amine.clinic.model.Specialty;
import com.amine.clinic.repository.DoctorRepository;
import com.amine.clinic.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, SpecialtyRepository specialtyRepository) {
        this.doctorRepository = doctorRepository;
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public DoctorResponseDTO create(DoctorRequestDTO request) {
        checkEmailUnique(request.getEmail(), null);
        checkLicenseUnique(request.getLicenseNumber(), null);
        Specialty specialty = getSpecialty(request.getSpecialtyId());
        Doctor doctor = new Doctor();
        mapRequestToEntity(request, doctor, specialty);
        return toResponse(doctorRepository.save(doctor));
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponseDTO findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> findAll() {
        return doctorRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public DoctorResponseDTO update(Long id, DoctorRequestDTO request) {
        Doctor doctor = getOrThrow(id);
        checkEmailUnique(request.getEmail(), id);
        checkLicenseUnique(request.getLicenseNumber(), id);
        Specialty specialty = getSpecialty(request.getSpecialtyId());
        mapRequestToEntity(request, doctor, specialty);
        return toResponse(doctorRepository.save(doctor));
    }

    @Override
    public void delete(Long id) {
        Doctor doctor = getOrThrow(id);
        doctorRepository.delete(doctor);
    }

    private void checkEmailUnique(String email, Long currentId) {
        doctorRepository.findByEmail(email).ifPresent(existing -> {
            if (currentId == null || !existing.getId().equals(currentId)) {
                throw new DuplicateResourceException("Doctor with email '" + email + "' already exists");
            }
        });
    }

    private void checkLicenseUnique(String licenseNumber, Long currentId) {
        if (licenseNumber == null || licenseNumber.isBlank()) {
            return;
        }
        doctorRepository.findByLicenseNumber(licenseNumber).ifPresent(existing -> {
            if (currentId == null || !existing.getId().equals(currentId)) {
                throw new DuplicateResourceException("Doctor with license number '" + licenseNumber + "' already exists");
            }
        });
    }

    private Specialty getSpecialty(Long specialtyId) {
        return specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialty not found with id: " + specialtyId));
    }

    private Doctor getOrThrow(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    private void mapRequestToEntity(DoctorRequestDTO request, Doctor doctor, Specialty specialty) {
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setSpecialty(specialty);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> findBySpecialty(Long specialtyId) {
        return doctorRepository.findBySpecialtyId(specialtyId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> findAvailable(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return doctorRepository.findDoctorsWithNoAppointmentsOnDate(start, end).stream().map(this::toResponse).toList();
    }

    private DoctorResponseDTO toResponse(Doctor doctor) {
        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getEmail(),
                doctor.getPhone(),
                doctor.getLicenseNumber(),
                doctor.getSpecialty() != null ? doctor.getSpecialty().getId() : null,
                doctor.getSpecialty() != null ? doctor.getSpecialty().getName() : null
        );
    }
}

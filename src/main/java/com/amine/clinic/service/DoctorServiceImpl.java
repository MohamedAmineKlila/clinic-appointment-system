package com.amine.clinic.service;

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
    public Doctor create(Doctor doctor) {
        checkEmailUnique(doctor.getEmail(), null);
        checkLicenseUnique(doctor.getLicenseNumber(), null);
        doctor.setSpecialty(getSpecialty(doctor.getSpecialty().getId()));
        return doctorRepository.save(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public Doctor findById(Long id) {
        return getOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor update(Long id, Doctor request) {
        Doctor doctor = getOrThrow(id);
        checkEmailUnique(request.getEmail(), id);
        checkLicenseUnique(request.getLicenseNumber(), id);
        Specialty specialty = getSpecialty(request.getSpecialty().getId());
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setSpecialty(specialty);
        return doctorRepository.save(doctor);
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

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> findBySpecialty(Long specialtyId) {
        return doctorRepository.findBySpecialty_Id(specialtyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> findAvailable(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return doctorRepository.findDoctorsWithNoAppointmentsOnDate(start, end);
    }
}

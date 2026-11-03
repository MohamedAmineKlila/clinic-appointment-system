package com.amine.clinic.service;

import com.amine.clinic.exception.BusinessRuleException;
import com.amine.clinic.exception.DuplicateResourceException;
import com.amine.clinic.exception.ResourceNotFoundException;
import com.amine.clinic.model.Appointment;
import com.amine.clinic.model.AppointmentStatus;
import com.amine.clinic.model.Prescription;
import com.amine.clinic.repository.AppointmentRepository;
import com.amine.clinic.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Prescription create(Prescription prescription) {
        if (prescription.getAppointment() != null) {
            Appointment appointment = getAppointment(prescription.getAppointment().getId());
            checkAppointmentStatus(appointment);
            prescriptionRepository.findByAppointment_Id(appointment.getId()).ifPresent(existing -> {
                throw new DuplicateResourceException("Prescription already exists for appointment id: " + appointment.getId());
            });
            prescription.setAppointment(appointment);
        }
        if (prescription.getIssuedDate() == null) {
            prescription.setIssuedDate(LocalDate.now());
        }
        return prescriptionRepository.save(prescription);
    }

    @Override
    @Transactional(readOnly = true)
    public Prescription findById(Long id) {
        return getOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescription> findAll() {
        return prescriptionRepository.findAll();
    }

    @Override
    public Prescription update(Long id, Prescription request) {
        Prescription prescription = getOrThrow(id);
        if (request.getAppointment() != null) {
            Appointment appointment = getAppointment(request.getAppointment().getId());
            checkAppointmentStatus(appointment);
            prescriptionRepository.findByAppointment_Id(appointment.getId()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new DuplicateResourceException("Prescription already exists for appointment id: " + appointment.getId());
                }
            });
            prescription.setAppointment(appointment);
        } else {
            prescription.setAppointment(null);
        }
        prescription.setMedication(request.getMedication());
        prescription.setDosage(request.getDosage());
        prescription.setInstructions(request.getInstructions());
        prescription.setIssuedDate(request.getIssuedDate());
        if (prescription.getIssuedDate() == null) {
            prescription.setIssuedDate(LocalDate.now());
        }
        return prescriptionRepository.save(prescription);
    }

    @Override
    public void delete(Long id) {
        Prescription prescription = getOrThrow(id);
        prescriptionRepository.delete(prescription);
    }

    private void checkAppointmentStatus(Appointment appointment) {
        if (appointment.getStatus() != AppointmentStatus.BOOKED && appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new BusinessRuleException("Prescription can only be created for an appointment with status BOOKED or COMPLETED");
        }
    }

    private Appointment getAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
    }

    private Prescription getOrThrow(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
    }
}

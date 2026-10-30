package com.amine.clinic.service;

import com.amine.clinic.dto.PrescriptionRequestDTO;
import com.amine.clinic.dto.PrescriptionResponseDTO;
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
    public PrescriptionResponseDTO create(PrescriptionRequestDTO request) {
        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            final Appointment found = getAppointment(request.getAppointmentId());
            appointment = found;
            if (found.getStatus() != AppointmentStatus.BOOKED && found.getStatus() != AppointmentStatus.COMPLETED) {
                throw new BusinessRuleException("Prescription can only be created for an appointment with status BOOKED or COMPLETED");
            }
            prescriptionRepository.findByAppointmentId(found.getId()).ifPresent(existing -> {
                throw new DuplicateResourceException("Prescription already exists for appointment id: " + found.getId());
            });
        }
        Prescription prescription = new Prescription();
        mapRequestToEntity(request, prescription, appointment);
        if (prescription.getIssuedDate() == null) {
            prescription.setIssuedDate(LocalDate.now());
        }
        return toResponse(prescriptionRepository.save(prescription));
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionResponseDTO findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDTO> findAll() {
        return prescriptionRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public PrescriptionResponseDTO update(Long id, PrescriptionRequestDTO request) {
        Prescription prescription = getOrThrow(id);
        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            final Appointment found = getAppointment(request.getAppointmentId());
            appointment = found;
            if (found.getStatus() != AppointmentStatus.BOOKED && found.getStatus() != AppointmentStatus.COMPLETED) {
                throw new BusinessRuleException("Prescription can only be created for an appointment with status BOOKED or COMPLETED");
            }
            prescriptionRepository.findByAppointmentId(found.getId()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new DuplicateResourceException("Prescription already exists for appointment id: " + found.getId());
                }
            });
        }
        mapRequestToEntity(request, prescription, appointment);
        if (prescription.getIssuedDate() == null) {
            prescription.setIssuedDate(LocalDate.now());
        }
        return toResponse(prescriptionRepository.save(prescription));
    }

    @Override
    public void delete(Long id) {
        Prescription prescription = getOrThrow(id);
        prescriptionRepository.delete(prescription);
    }

    private Appointment getAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
    }

    private Prescription getOrThrow(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
    }

    private void mapRequestToEntity(PrescriptionRequestDTO request, Prescription prescription, Appointment appointment) {
        prescription.setMedication(request.getMedication());
        prescription.setDosage(request.getDosage());
        prescription.setInstructions(request.getInstructions());
        prescription.setIssuedDate(request.getIssuedDate());
        prescription.setAppointment(appointment);
    }

    private PrescriptionResponseDTO toResponse(Prescription prescription) {
        return new PrescriptionResponseDTO(
                prescription.getId(),
                prescription.getMedication(),
                prescription.getDosage(),
                prescription.getInstructions(),
                prescription.getIssuedDate(),
                prescription.getAppointment() != null ? prescription.getAppointment().getId() : null
        );
    }
}

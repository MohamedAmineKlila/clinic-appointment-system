package com.amine.clinic.service;

import com.amine.clinic.dto.AppointmentRequestDTO;
import com.amine.clinic.dto.AppointmentResponseDTO;
import com.amine.clinic.exception.AppointmentConflictException;
import com.amine.clinic.exception.BusinessRuleException;
import com.amine.clinic.exception.ResourceNotFoundException;
import com.amine.clinic.model.Appointment;
import com.amine.clinic.model.AppointmentStatus;
import com.amine.clinic.model.Doctor;
import com.amine.clinic.model.Patient;
import com.amine.clinic.repository.AppointmentRepository;
import com.amine.clinic.repository.DoctorRepository;
import com.amine.clinic.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                 DoctorRepository doctorRepository,
                                 PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public AppointmentResponseDTO create(AppointmentRequestDTO request) {
        Doctor doctor = getDoctor(request.getDoctorId());
        Patient patient = getPatient(request.getPatientId());
        validateFuture(request.getAppointmentDateTime());
        AppointmentStatus status = request.getStatus() != null ? request.getStatus() : AppointmentStatus.BOOKED;
        if (status == AppointmentStatus.BOOKED) {
            ensureNoConflict(doctor.getId(), request.getAppointmentDateTime(), null);
        }
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(request.getAppointmentDateTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(status);
        return toResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDTO findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> findAll() {
        return appointmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public AppointmentResponseDTO update(Long id, AppointmentRequestDTO request) {
        Appointment appointment = getOrThrow(id);
        Doctor doctor = getDoctor(request.getDoctorId());
        Patient patient = getPatient(request.getPatientId());
        validateFuture(request.getAppointmentDateTime());
        AppointmentStatus status = request.getStatus() != null ? request.getStatus() : AppointmentStatus.BOOKED;
        if (status == AppointmentStatus.BOOKED) {
            ensureNoConflict(doctor.getId(), request.getAppointmentDateTime(), id);
        }
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(request.getAppointmentDateTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(status);
        return toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public void delete(Long id) {
        Appointment appointment = getOrThrow(id);
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BusinessRuleException("Cannot delete a COMPLETED appointment");
        }
        appointmentRepository.delete(appointment);
    }

    private Doctor getDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
    }

    private Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
    }

    private Appointment getOrThrow(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    private void validateFuture(LocalDateTime dateTime) {
        if (dateTime == null || !dateTime.isAfter(LocalDateTime.now())) {
            throw new BusinessRuleException("appointmentDateTime must be in the future");
        }
    }

    private void ensureNoConflict(Long doctorId, LocalDateTime dateTime, Long excludeId) {
        List<Appointment> sameDay = appointmentRepository.findAppointmentsByDoctorAndDate(
                doctorId, dateTime.toLocalDate().atStartOfDay(), dateTime.toLocalDate().plusDays(1).atStartOfDay());
        boolean conflict = sameDay.stream().anyMatch(a ->
                a.getDateTime().equals(dateTime)
                        && a.getStatus() == AppointmentStatus.BOOKED
                        && (excludeId == null || !a.getId().equals(excludeId)));
        if (conflict) {
            throw new AppointmentConflictException("Doctor already has a BOOKED appointment at the same date and time");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> findByDoctorAndDate(Long doctorId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return appointmentRepository.findAppointmentsByDoctorAndDate(doctorId, start, end).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> findByPatient(Long patientId) {
        return appointmentRepository.findAppointmentsByPatientId(patientId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(AppointmentStatus status) {
        return appointmentRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> findByStatusAndDateRange(AppointmentStatus status, LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59, 999999999);
        return appointmentRepository.findAppointmentsByStatusAndDateRange(status, startDateTime, endDateTime).stream()
                .map(this::toResponse).toList();
    }

    private AppointmentResponseDTO toResponse(Appointment appointment) {
        String doctorName = null;
        if (appointment.getDoctor() != null) {
            doctorName = appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
        }
        String patientName = null;
        if (appointment.getPatient() != null) {
            patientName = appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName();
        }
        Long prescriptionId = (appointment.getPrescription() != null) ? appointment.getPrescription().getId() : null;
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getDoctor() != null ? appointment.getDoctor().getId() : null,
                doctorName,
                appointment.getPatient() != null ? appointment.getPatient().getId() : null,
                patientName,
                appointment.getDateTime(),
                appointment.getReason(),
                appointment.getStatus(),
                prescriptionId
        );
    }
}

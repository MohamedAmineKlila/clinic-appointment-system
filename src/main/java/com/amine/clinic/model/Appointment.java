package com.amine.clinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "doctorId", "doctorName", "patientId", "patientName",
        "appointmentDateTime", "reason", "status", "prescriptionId"})
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @JsonIgnore
    @NotNull
    @Future(message = "appointment date must be in the future")
    @Column(nullable = false)
    private LocalDateTime dateTime;

    @NotBlank
    @Size(max = 300)
    @Column(length = 300)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status = AppointmentStatus.BOOKED;

    /**
     * Optional one-to-one: an appointment may exist without a prescription.
     * The prescription owns the foreign key; this side is the inverse, read-only
     * side, so no join column is placed here and orphan removal is disabled.
     */
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private Prescription prescription;

    // ------------------------------------------------------------------
    // Flat JSON accessors. The API keeps the same simple contract as the
    // entities themselves: clients send the related entity's id and receive
    // the id (and a display name) back, while the object references stay
    // out of the JSON so no cycle can form between the entities.
    // ------------------------------------------------------------------

    public Long getDoctorId() {
        return doctor != null ? doctor.getId() : null;
    }

    public void setDoctorId(Long doctorId) {
        if (doctorId == null) {
            this.doctor = null;
        } else {
            Doctor reference = new Doctor();
            reference.setId(doctorId);
            this.doctor = reference;
        }
    }

    public String getDoctorName() {
        if (doctor == null || doctor.getFirstName() == null) {
            return null;
        }
        return doctor.getFirstName() + " " + doctor.getLastName();
    }

    public Long getPatientId() {
        return patient != null ? patient.getId() : null;
    }

    public void setPatientId(Long patientId) {
        if (patientId == null) {
            this.patient = null;
        } else {
            Patient reference = new Patient();
            reference.setId(patientId);
            this.patient = reference;
        }
    }

    public String getPatientName() {
        if (patient == null || patient.getFirstName() == null) {
            return null;
        }
        return patient.getFirstName() + " " + patient.getLastName();
    }

    public LocalDateTime getAppointmentDateTime() {
        return dateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.dateTime = appointmentDateTime;
    }

    public Long getPrescriptionId() {
        return prescription != null ? prescription.getId() : null;
    }
}
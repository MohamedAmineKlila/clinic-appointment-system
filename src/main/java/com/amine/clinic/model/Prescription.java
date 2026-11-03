package com.amine.clinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "medication", "dosage", "instructions", "issuedDate", "appointmentId"})
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 200, nullable = false)
    private String medication;

    @Size(max = 200)
    private String dosage;

    @NotBlank
    @Column(length = 1000, nullable = false)
    private String instructions;

    @Column(name = "issued_date")
    private LocalDate issuedDate;

    /**
     * Optional one-to-one with appointment. A prescription may exist
     * independently or be attached to an appointment.
     */
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", unique = true)
    private Appointment appointment;

    // Flat JSON accessor: the API exchanges the appointment object for its id.
    public Long getAppointmentId() {
        return appointment != null ? appointment.getId() : null;
    }

    public void setAppointmentId(Long appointmentId) {
        if (appointmentId == null) {
            this.appointment = null;
        } else {
            Appointment reference = new Appointment();
            reference.setId(appointmentId);
            this.appointment = reference;
        }
    }
}
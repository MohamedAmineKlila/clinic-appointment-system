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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "bloodType", "allergies", "chronicConditions", "patientId"})
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blood_type", length = 10)
    private String bloodType;

    @Column(length = 1000)
    private String allergies;

    @Column(length = 1000)
    private String chronicConditions;

    /**
     * One-to-one with patient. The medical record owns the foreign key
     * (patient_id) and is placed on the owning side.
     */
    @JsonIgnore
    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    // Flat JSON accessor: the API exchanges the patient object for its id.
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
}
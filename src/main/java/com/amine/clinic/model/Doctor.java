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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "firstName", "lastName", "email", "phone", "licenseNumber",
        "specialtyId", "specialtyName"})
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String firstName;

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 120)
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String phone;

    @Size(max = 150)
    @Column(length = 150)
    private String licenseNumber;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    // Flat JSON accessors: the API exposes the specialty as an id plus a
    // name instead of the nested object (which could recurse back here).
    public Long getSpecialtyId() {
        return specialty != null ? specialty.getId() : null;
    }

    public void setSpecialtyId(Long specialtyId) {
        if (specialtyId == null) {
            this.specialty = null;
        } else {
            Specialty reference = new Specialty();
            reference.setId(specialtyId);
            this.specialty = reference;
        }
    }

    public String getSpecialtyName() {
        return specialty != null ? specialty.getName() : null;
    }
}

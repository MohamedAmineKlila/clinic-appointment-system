package com.amine.clinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "firstName", "lastName", "email", "phone", "dateOfBirth"})
public class Patient {

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

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @JsonIgnore
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();
}
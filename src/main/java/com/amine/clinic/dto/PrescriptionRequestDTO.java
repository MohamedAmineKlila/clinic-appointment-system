package com.amine.clinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequestDTO {

    @NotBlank
    @Size(max = 200)
    private String medication;

    @Size(max = 200)
    private String dosage;

    @NotBlank
    @Size(max = 1000)
    private String instructions;

    private LocalDate issuedDate;

    private Long appointmentId;
}

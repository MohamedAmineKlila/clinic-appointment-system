package com.amine.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponseDTO {

    private Long id;
    private String medication;
    private String dosage;
    private String instructions;
    private LocalDate issuedDate;
    private Long appointmentId;
}

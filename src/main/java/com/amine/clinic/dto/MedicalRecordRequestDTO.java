package com.amine.clinic.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordRequestDTO {

    @Size(max = 10)
    private String bloodType;

    @Size(max = 1000)
    private String allergies;

    @Size(max = 1000)
    private String chronicConditions;

    @NotNull
    private Long patientId;
}

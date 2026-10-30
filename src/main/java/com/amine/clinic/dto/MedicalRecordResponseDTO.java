package com.amine.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponseDTO {

    private Long id;
    private String bloodType;
    private String allergies;
    private String chronicConditions;
    private Long patientId;
}

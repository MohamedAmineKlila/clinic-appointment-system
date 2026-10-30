package com.amine.clinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialtyRequestDTO {

    @NotBlank
    @Size(max = 80)
    private String name;

    @Size(max = 300)
    private String description;
}

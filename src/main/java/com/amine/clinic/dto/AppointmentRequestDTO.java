package com.amine.clinic.dto;

import com.amine.clinic.model.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

    @NotNull
    private Long doctorId;

    @NotNull
    private Long patientId;

    @NotNull
    @Future
    private LocalDateTime appointmentDateTime;

    @NotBlank
    @Size(max = 300)
    private String reason;

    private AppointmentStatus status;
}

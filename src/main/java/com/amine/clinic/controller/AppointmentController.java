package com.amine.clinic.controller;

import com.amine.clinic.model.Appointment;

import com.amine.clinic.model.AppointmentStatus;
import com.amine.clinic.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> findAll() {
        return ResponseEntity.ok(appointmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> findById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@Valid @RequestBody Appointment request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> update(@PathVariable Long id, @Valid @RequestBody Appointment request) {
        return ResponseEntity.ok(appointmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<List<Appointment>> findByDoctorAndDate(@PathVariable Long doctorId,
                                                                          @PathVariable LocalDate date) {
        return ResponseEntity.ok(appointmentService.findByDoctorAndDate(doctorId, date));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> findByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.findByPatient(patientId));
    }

    @GetMapping("/status/{status}/count")
    public ResponseEntity<Long> countByStatus(@PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.countByStatus(status));
    }

    @GetMapping("/status/{status}/date-range")
    public ResponseEntity<List<Appointment>> findByStatusAndDateRange(
            @PathVariable AppointmentStatus status,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(appointmentService.findByStatusAndDateRange(status, start, end));
    }
}

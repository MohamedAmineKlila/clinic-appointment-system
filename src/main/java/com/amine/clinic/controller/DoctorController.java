package com.amine.clinic.controller;

import com.amine.clinic.model.Doctor;

import com.amine.clinic.service.DoctorService;
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
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> findAll() {
        return ResponseEntity.ok(doctorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> findById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Doctor> create(@Valid @RequestBody Doctor request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> update(@PathVariable Long id, @Valid @RequestBody Doctor request) {
        return ResponseEntity.ok(doctorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<Doctor>> findAvailable(@RequestParam LocalDate date) {
        return ResponseEntity.ok(doctorService.findAvailable(date));
    }

    @GetMapping("/specialty/{specialtyId}")
    public ResponseEntity<List<Doctor>> findBySpecialty(@PathVariable Long specialtyId) {
        return ResponseEntity.ok(doctorService.findBySpecialty(specialtyId));
    }
}

package com.amine.clinic.controller;

import com.amine.clinic.dto.PrescriptionRequestDTO;
import com.amine.clinic.dto.PrescriptionResponseDTO;
import com.amine.clinic.service.PrescriptionService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionResponseDTO>> findAll() {
        return ResponseEntity.ok(prescriptionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PrescriptionResponseDTO> create(@Valid @RequestBody PrescriptionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PrescriptionRequestDTO request) {
        return ResponseEntity.ok(prescriptionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prescriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.amine.clinic.controller;

import com.amine.clinic.model.MedicalRecord;

import com.amine.clinic.service.MedicalRecordService;
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
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecord>> findAll() {
        return ResponseEntity.ok(medicalRecordService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> findById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> create(@Valid @RequestBody MedicalRecord request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalRecordService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> update(@PathVariable Long id, @Valid @RequestBody MedicalRecord request) {
        return ResponseEntity.ok(medicalRecordService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicalRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

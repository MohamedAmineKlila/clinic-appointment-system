package com.amine.clinic.controller;

import com.amine.clinic.model.Specialty;

import com.amine.clinic.service.SpecialtyService;
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
@RequestMapping("/api/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public ResponseEntity<List<Specialty>> findAll() {
        return ResponseEntity.ok(specialtyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialty> findById(@PathVariable Long id) {
        return ResponseEntity.ok(specialtyService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Specialty> create(@Valid @RequestBody Specialty request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(specialtyService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialty> update(@PathVariable Long id, @Valid @RequestBody Specialty request) {
        return ResponseEntity.ok(specialtyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        specialtyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

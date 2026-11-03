package com.amine.clinic.repository;

import com.amine.clinic.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    Optional<MedicalRecord> findByPatient_Id(Long patientId);
}

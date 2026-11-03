package com.amine.clinic.service;

import com.amine.clinic.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {

    MedicalRecord create(MedicalRecord record);

    MedicalRecord findById(Long id);

    List<MedicalRecord> findAll();

    MedicalRecord update(Long id, MedicalRecord record);

    void delete(Long id);
}

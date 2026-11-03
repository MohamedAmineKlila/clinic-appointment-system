package com.amine.clinic.repository;

import com.amine.clinic.model.Appointment;
import com.amine.clinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecialty_Id(Long specialtyId);

    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    @Query("SELECT d FROM Doctor d WHERE d.id NOT IN (SELECT a.doctor.id FROM Appointment a WHERE a.dateTime >= :start AND a.dateTime < :end)")
    List<Doctor> findDoctorsWithNoAppointmentsOnDate(@Param("start") java.time.LocalDateTime start,
                                                   @Param("end") java.time.LocalDateTime end);
}

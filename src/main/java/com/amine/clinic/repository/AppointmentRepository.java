package com.amine.clinic.repository;

import com.amine.clinic.model.Appointment;
import com.amine.clinic.model.AppointmentStatus;
import com.amine.clinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.dateTime >= :start AND a.dateTime < :end")
    List<Appointment> findAppointmentsByDoctorAndDate(@Param("doctorId") Long doctorId,
                                                     @Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end);

    default List<Appointment> findAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        return findAppointmentsByDoctorAndDate(doctorId, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId")
    List<Appointment> findAppointmentsByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = :status")
    long countByStatus(@Param("status") AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.status = :status AND a.dateTime >= :start AND a.dateTime <= :end")
    List<Appointment> findAppointmentsByStatusAndDateRange(@Param("status") AppointmentStatus status,
                                                          @Param("start") LocalDateTime start,
                                                          @Param("end") LocalDateTime end);

    @Query("SELECT d FROM Doctor d WHERE d.id NOT IN (SELECT a.doctor.id FROM Appointment a WHERE a.dateTime >= :start AND a.dateTime < :end)")
    List<Doctor> findDoctorsWithNoAppointmentsOnDate(@Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end);

    default List<Doctor> findDoctorsWithNoAppointmentsOnDate(LocalDate date) {
        return findDoctorsWithNoAppointmentsOnDate(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }
}

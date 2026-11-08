package com.amine.clinic.config;

import com.amine.clinic.model.Appointment;
import com.amine.clinic.model.AppointmentStatus;
import com.amine.clinic.model.Doctor;
import com.amine.clinic.model.MedicalRecord;
import com.amine.clinic.model.Patient;
import com.amine.clinic.model.Prescription;
import com.amine.clinic.model.Specialty;
import com.amine.clinic.repository.AppointmentRepository;
import com.amine.clinic.repository.DoctorRepository;
import com.amine.clinic.repository.MedicalRecordRepository;
import com.amine.clinic.repository.PatientRepository;
import com.amine.clinic.repository.PrescriptionRepository;
import com.amine.clinic.repository.SpecialtyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SpecialtyRepository specialtyRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    public DataSeeder(SpecialtyRepository specialtyRepository,
                      DoctorRepository doctorRepository,
                      PatientRepository patientRepository,
                      MedicalRecordRepository medicalRecordRepository,
                      AppointmentRepository appointmentRepository,
                      PrescriptionRepository prescriptionRepository) {
        this.specialtyRepository = specialtyRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (specialtyRepository.count() > 0 || doctorRepository.count() > 0
                || patientRepository.count() > 0 || appointmentRepository.count() > 0) {
            return;
        }

        Specialty cardiology = saveSpecialty("Cardiology", "Heart and cardiovascular system");
        Specialty pediatrics = saveSpecialty("Pediatrics", "Medical care for children");
        Specialty dermatology = saveSpecialty("Dermatology", "Skin, hair, and nails");
        Specialty orthopedics = saveSpecialty("Orthopedics", "Bones, joints, and muscles");

        Doctor doctor1 = saveDoctor("Mohamed", "Ben Ali", "mohamed.benali@clinic.com", "1234567890", "LIC001", cardiology);
        Doctor doctor2 = saveDoctor("Fatma", "Trabelsi", "fatma.trabelsi@clinic.com", "1234567891", "LIC002", cardiology);
        Doctor doctor3 = saveDoctor("Ahmed", "Mejri", "ahmed.mejri@clinic.com", "1234567892", "LIC003", pediatrics);
        Doctor doctor4 = saveDoctor("Leila", "Gharbi", "leila.gharbi@clinic.com", "1234567893", "LIC004", dermatology);
        Doctor doctor5 = saveDoctor("Karim", "Jaziri", "karim.jaziri@clinic.com", "1234567894", "LIC005", orthopedics);

        Patient patient1 = savePatient("Amira", "Bouazizi", "amira.bouazizi@email.com", "9876543210", LocalDate.of(1990, 5, 12));
        Patient patient2 = savePatient("Youssef", "Khelifi", "youssef.khelifi@email.com", "9876543211", LocalDate.of(1985, 8, 23));
        Patient patient3 = savePatient("Nadia", "Hammami", "nadia.hammami@email.com", "9876543212", LocalDate.of(1995, 2, 14));
        Patient patient4 = savePatient("Slim", "Ben Salah", "slim.bensalah@email.com", "9876543213", LocalDate.of(1988, 11, 3));
        Patient patient5 = savePatient("Mariem", "Chouchane", "mariem.chouchane@email.com", "9876543214", LocalDate.of(1992, 7, 19));

        saveMedicalRecord(patient1, "A+", "Peanuts", "None");
        saveMedicalRecord(patient2, "O-", "None", "Hypertension");
        saveMedicalRecord(patient3, "B+", "Penicillin", "Asthma");
        saveMedicalRecord(patient4, "AB+", "None", "Diabetes");
        // patient 5 intentionally starts without a medical record so the Postman
        // collection can demonstrate creating one (Medical Records - Create).

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstSlot = now.plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
        // The Postman collection books doctor 1 on 2026-12-01T09:00. On the one day
        // a year when this seeded slot would land on that exact date and time, shift
        // it by 30 minutes so the collection still runs cleanly on a fresh database.
        if (firstSlot.equals(LocalDateTime.of(2026, 12, 1, 9, 0))) {
            firstSlot = firstSlot.plusMinutes(30);
        }
        Appointment a1 = saveAppointment(doctor1, patient1, firstSlot, "Routine checkup", AppointmentStatus.BOOKED);
        Appointment a2 = saveAppointment(doctor1, patient2, now.plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0), "Follow-up visit", AppointmentStatus.COMPLETED);
        Appointment a3 = saveAppointment(doctor2, patient3, now.plusDays(3).withHour(11).withMinute(0).withSecond(0).withNano(0), "Consultation", AppointmentStatus.BOOKED);
        Appointment a4 = saveAppointment(doctor3, patient4, now.plusDays(4).withHour(14).withMinute(0).withSecond(0).withNano(0), "Pediatric review", AppointmentStatus.CANCELLED);
        Appointment a5 = saveAppointment(doctor4, patient5, now.plusDays(5).withHour(9).withMinute(30).withSecond(0).withNano(0), "Skin examination", AppointmentStatus.COMPLETED);
        Appointment a6 = saveAppointment(doctor2, patient1, now.plusDays(6).withHour(10).withMinute(30).withSecond(0).withNano(0), "Heart monitoring", AppointmentStatus.BOOKED);
        Appointment a7 = saveAppointment(doctor5, patient2, now.plusDays(7).withHour(15).withMinute(0).withSecond(0).withNano(0), "Joint pain", AppointmentStatus.COMPLETED);
        Appointment a8 = saveAppointment(doctor3, patient3, now.plusDays(8).withHour(11).withMinute(30).withSecond(0).withNano(0), "Vaccination", AppointmentStatus.CANCELLED);

        savePrescription(a2, "Aspirin", "100mg", "Take once daily", LocalDate.now());
        savePrescription(a5, "Hydrocortisone cream", "Apply thin layer", "Apply twice daily", LocalDate.now());
        savePrescription(a1, "Vitamin D", "1000 IU", "Take one capsule daily", LocalDate.now());
    }

    private Specialty saveSpecialty(String name, String description) {
        Specialty specialty = new Specialty();
        specialty.setName(name);
        specialty.setDescription(description);
        return specialtyRepository.save(specialty);
    }

    private Doctor saveDoctor(String firstName, String lastName, String email, String phone, String licenseNumber, Specialty specialty) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setEmail(email);
        doctor.setPhone(phone);
        doctor.setLicenseNumber(licenseNumber);
        doctor.setSpecialty(specialty);
        return doctorRepository.save(doctor);
    }

    private Patient savePatient(String firstName, String lastName, String email, String phone, LocalDate dateOfBirth) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setEmail(email);
        patient.setPhone(phone);
        patient.setDateOfBirth(dateOfBirth);
        return patientRepository.save(patient);
    }

    private void saveMedicalRecord(Patient patient, String bloodType, String allergies, String chronicConditions) {
        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setBloodType(bloodType);
        record.setAllergies(allergies);
        record.setChronicConditions(chronicConditions);
        medicalRecordRepository.save(record);
    }

    private Appointment saveAppointment(Doctor doctor, Patient patient, LocalDateTime dateTime, String reason, AppointmentStatus status) {
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(dateTime);
        appointment.setReason(reason);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    private void savePrescription(Appointment appointment, String medication, String dosage, String instructions, LocalDate issuedDate) {
        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setMedication(medication);
        prescription.setDosage(dosage);
        prescription.setInstructions(instructions);
        prescription.setIssuedDate(issuedDate);
        prescriptionRepository.save(prescription);
    }
}

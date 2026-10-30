# Clinic Appointment Management System

## Business context
The Clinic Appointment Management System is a Spring Boot REST API for managing clinic operations. It helps clinics manage specialties, doctors, patients, appointments, medical records, and prescriptions. The system enforces business rules such as unique emails and license numbers, one medical record per patient, one prescription per appointment, and prevents conflicting doctor appointments.

## Technologies used
- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Lombok
- Postman

## Entity list and relationships
- Specialty: id, name
- Doctor: id, firstName, lastName, email, phone, licenseNumber; belongs to one Specialty
- Patient: id, firstName, lastName, email, phone, dateOfBirth; has one MedicalRecord
- Appointment: id, appointmentDateTime, status; belongs to one Doctor and one Patient; may have one Prescription
- MedicalRecord: id, bloodType, allergies, chronicConditions; belongs to one Patient
- Prescription: id, medication, dosage, instructions, issuedDate; belongs to one Appointment

Relationships:
- Specialty OneToMany Doctor
- Doctor OneToMany Appointment
- Patient OneToMany Appointment
- Patient OneToOne MedicalRecord
- Appointment OneToOne Prescription (optional)

## REST API endpoints
| Method | Endpoint | Description |
|---|---|---|
| GET | /api/specialties | List all specialties |
| GET | /api/specialties/{id} | Get specialty by id |
| POST | /api/specialties | Create specialty |
| PUT | /api/specialties/{id} | Update specialty |
| DELETE | /api/specialties/{id} | Delete specialty |
| GET | /api/doctors | List all doctors |
| GET | /api/doctors/{id} | Get doctor by id |
| POST | /api/doctors | Create doctor |
| PUT | /api/doctors/{id} | Update doctor |
| DELETE | /api/doctors/{id} | Delete doctor |
| GET | /api/doctors/available?date=yyyy-MM-dd | Doctors with no appointments on date |
| GET | /api/doctors/specialty/{specialtyId} | Doctors by specialty |
| GET | /api/patients | List all patients |
| GET | /api/patients/{id} | Get patient by id |
| POST | /api/patients | Create patient |
| PUT | /api/patients/{id} | Update patient |
| DELETE | /api/patients/{id} | Delete patient |
| GET | /api/appointments | List all appointments |
| GET | /api/appointments/{id} | Get appointment by id |
| POST | /api/appointments | Create appointment |
| PUT | /api/appointments/{id} | Update appointment |
| DELETE | /api/appointments/{id} | Delete appointment |
| GET | /api/appointments/doctor/{doctorId}/date/{date} | Appointments for doctor on date |
| GET | /api/appointments/patient/{patientId} | Appointments for patient |
| GET | /api/appointments/status/{status}/count | Count appointments by status |
| GET | /api/appointments/status/{status}/date-range?start=yyyy-MM-dd&end=yyyy-MM-dd | Appointments by status and date range |
| GET | /api/medical-records | List all medical records |
| GET | /api/medical-records/{id} | Get medical record by id |
| POST | /api/medical-records | Create medical record |
| PUT | /api/medical-records/{id} | Update medical record |
| DELETE | /api/medical-records/{id} | Delete medical record |
| GET | /api/prescriptions | List all prescriptions |
| GET | /api/prescriptions/{id} | Get prescription by id |
| POST | /api/prescriptions | Create prescription |
| PUT | /api/prescriptions/{id} | Update prescription |
| DELETE | /api/prescriptions/{id} | Delete prescription |

## Custom business queries
- Find all appointments for a specific doctor on a specific date.
- Find all appointments for a specific patient.
- Count appointments by status.
- Find appointments by status and date range.
- Find doctors who have no appointments on a specific date.
- Find doctors by specialty ID.
- Find patient by email.
- Find medical record by patient ID.
- Find prescription by appointment ID.

## Validation and error handling
Request DTOs use Jakarta Bean Validation annotations such as @NotBlank, @Email, @Size, @NotNull, @Future, and phone number patterns. Controllers use @Valid. A global exception handler returns a consistent JSON error structure with timestamp, status, error, message, path, and field-level details for validation errors. HTTP status codes: 200, 201, 204, 400, 404, 409, 500.

## MySQL setup instructions
1. Install MySQL and start the service.
2. Create the database if needed: CREATE DATABASE clinic_db;
3. Update src/main/resources/application.properties with your MySQL username and password.
4. Ensure MySQL runs on port 3306.

## How to run the project with Maven
1. Open a terminal in the project root.
2. Run: mvn spring-boot:run
3. The API will be available at http://localhost:8080

## How to import and use the Postman collection
1. Open Postman.
2. Click Import and select postman/ClinicAppointmentSystem.postman_collection.json.
3. Use http://localhost:8080 as the base URL.
4. Run requests individually or with the Collection Runner.

## Example JSON request bodies

Create doctor:
```json
{
  "firstName": "Mohamed",
  "lastName": "Ben Ali",
  "email": "mohamed.benali@clinic.com",
  "phone": "1234567890",
  "licenseNumber": "LIC001",
  "specialtyId": 1
}
```

Create patient:
```json
{
  "firstName": "Amira",
  "lastName": "Bouazizi",
  "email": "amira.bouazizi@email.com",
  "phone": "9876543210",
  "dateOfBirth": "1990-05-12"
}
```

Create appointment:
```json
{
  "doctorId": 1,
  "patientId": 1,
  "appointmentDateTime": "2026-11-15T09:00:00",
  "reason": "Routine checkup",
  "status": "BOOKED"
}
```

# Clinic Appointment Management System

## Business context
The Clinic Appointment Management System is a Spring Boot REST API for managing clinic operations. It helps clinics manage specialties, doctors, patients, appointments, medical records, and prescriptions. The system enforces business rules such as unique emails and license numbers, one medical record per patient, one prescription per appointment, and prevents conflicting doctor appointments.

## Project at a glance
- **36 REST API endpoints** across 6 resources (specialties, doctors, patients, appointments, medical records, prescriptions)
- **9 custom repository queries** for clinic-specific searches (appointments by doctor/date/patient/status, available doctors, doctors by specialty, and lookups by email, patient, and appointment)
- 6 JPA entities backed by MySQL, with Jakarta Bean Validation and a global exception handler

## Technologies used
- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Lombok
- Postman

## Project structure
The code follows the layered architecture of the course: each layer lives in its own package under `com.amine.clinic`, and the entities are passed directly between controller, service, and repository.

```
src/main/java/com/amine/clinic/
├── ClinicApplication.java   Spring Boot entry point
├── model/                   JPA entities + Jakarta Bean Validation annotations
├── repository/              Spring Data JPA repositories (CRUD + custom queries)
├── service/                 business logic and business-rule checks
├── controller/              REST controllers (endpoints and HTTP status codes)
├── exception/               custom exceptions + global exception handler
└── config/                  data seeder (initial sample data)

src/main/resources/
└── application.properties

src/test/java/com/amine/clinic/
└── service and context tests
```

## Entity list and relationships
- Specialty: id, name, description
- Doctor: id, firstName, lastName, email, phone, licenseNumber; belongs to one Specialty
- Patient: id, firstName, lastName, email, phone, dateOfBirth; has one MedicalRecord
- Appointment: id, appointmentDateTime, status, reason; belongs to one Doctor and one Patient; may have one Prescription
- MedicalRecord: id, bloodType, allergies, chronicConditions; belongs to one Patient
- Prescription: id, medication, dosage, instructions, issuedDate; belongs to one Appointment

Relationships:
- Specialty OneToMany Doctor
- Doctor OneToMany Appointment
- Patient OneToMany Appointment
- Patient OneToOne MedicalRecord
- Appointment OneToOne Prescription (optional)

## REST API endpoints (36 endpoints)
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

## Custom business queries (9 custom repository queries)
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
The JPA entities in `model/` carry Jakarta Bean Validation annotations such as @NotBlank, @Email, @Size, @NotNull, @Future, and phone number patterns. Controllers validate the received entity with @Valid before it reaches the service layer. A global exception handler returns a consistent JSON error structure with timestamp, status, error, message, path, and field-level details for validation errors. HTTP status codes: 200, 201, 204, 400, 404, 409, 500.

The split between 400 and 409 is deliberate: HTTP 400 Bad Request means the request itself is invalid (missing fields, malformed email, a date in the past), so the client should fix the payload. HTTP 409 Conflict means the request is valid but clashes with the current state of the data: double-booking a doctor at an exact BOOKED slot (AppointmentConflictException) or creating a specialty with a name that already exists (DuplicateResourceException). The conflict is detected in the service layer against existing rows, which is exactly what 409 is for.

## MySQL setup instructions
1. Install MySQL 8 and start the MySQL service.
2. Open the MySQL command line:
   ```bash
   mysql -u root -p
   ```
3. Create the database:
   ```sql
   CREATE DATABASE clinic_db;
   ```
4. Exit MySQL:
   ```sql
   exit;
   ```
5. Update `src/main/resources/application.properties` with your MySQL username and password, or set the `DB_PASSWORD` environment variable.
6. Ensure MySQL runs on port 3306.

## How to run the project with Maven
1. Open a terminal in the project root.
2. Run: mvn spring-boot:run
3. The API will be available at http://localhost:8080

## How to run tests
1. Open a terminal in the project root.
2. Run: mvn test
3. Tests use an in-memory H2 database configured in src/test/resources/application.properties.

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

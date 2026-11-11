# Grab Pic

## Overview
Grab Pic is an event photo distribution system. Organizers create an event space and upload photos, which are saved in original quality to disk and tracked in a MySQL database.

## Tech Stack
- Java 17
- Spring Boot 3.3.4 (Spring Web, Spring Data JPA, Spring Validation)
- MySQL
- Lombok
- Apache Maven

## Current Status
- Done:
  - Event creation and lookup (`POST /events`, `GET /events/{id}`)
  - Multi-file photo upload with original files stored unmodified on disk (`POST /events/{id}/photos`)
  - Database schema mapping for events and photos with `UPLOADED` status tracking
- In Progress:
  - Advanced upload validation (strict MIME checks, file size error handling)
  - Python-based face detection and embedding service
  - Java-based face clustering and matching pipeline

## Getting Started

### 1. Prerequisites
- Java 17 or higher
- Apache Maven 3.8+
- MySQL Server 8.0+

### 2. MySQL Database Setup
Ensure MySQL is running locally on port 3306. Create the database:
```sql
CREATE DATABASE grabpic_db;
```

### 3. Configuration
Copy the sample configuration file:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```
Update `src/main/resources/application.properties` with your MySQL username and password:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```
Alternatively, set the following environment variables:
- `DB_URL` (optional, defaults to local `grabpic_db`)
- `DB_USERNAME` (optional, defaults to `root`)
- `DB_PASSWORD` (optional, defaults to `root`)

### 4. Build and Run
Build the project using Maven:
```bash
mvn clean compile
```
Run the application:
```bash
mvn spring-boot:run
```
The server will start on `http://localhost:8080`.

## API Documentation

### 1. Create Event
- Method: `POST`
- URL: `http://localhost:8080/events`
- Headers: `Content-Type: application/json`
- Request Body:
```json
{
  "name": "Tech Fest 2026",
  "accessCode": "TECH26"
}
```
- Response: `201 Created`
```json
{
  "id": 1,
  "name": "Tech Fest 2026",
  "accessCode": "TECH26",
  "createdAt": "2026-11-06T18:30:00",
  "totalPhotos": 0
}
```

### 2. Get Event Details
- Method: `GET`
- URL: `http://localhost:8080/events/1`
- Response: `200 OK`
```json
{
  "id": 1,
  "name": "Tech Fest 2026",
  "accessCode": "TECH26",
  "createdAt": "2026-11-06T18:30:00",
  "totalPhotos": 0
}
```

### 3. Upload Event Photos
- Method: `POST`
- URL: `http://localhost:8080/events/1/photos`
- Headers: `Content-Type: multipart/form-data`
- Body (form-data):
  - Key: `files` (Type: File, select 1 or more images)
- Response: `201 Created`
```json
[
  {
    "id": 1,
    "eventId": 1,
    "originalPath": "C:\\Users\\...\\uploads\\1\\9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d.jpg",
    "status": "UPLOADED",
    "uploadedAt": "2026-11-06T18:31:00"
  }
]
```

## Project Structure
```
src/main/java/com/yash/grabpic/
├── GrabPicApplication.java
├── common/
│   ├── BadRequestException.java
│   └── ResourceNotFoundException.java
├── event/
│   ├── Event.java
│   ├── EventController.java
│   ├── EventRepository.java
│   ├── EventService.java
│   └── dto/
│       ├── CreateEventRequest.java
│       └── EventResponse.java
└── photo/
    ├── Photo.java
    ├── PhotoController.java
    ├── PhotoRepository.java
    ├── PhotoService.java
    ├── PhotoStatus.java
    └── dto/
        └── PhotoResponse.java
```

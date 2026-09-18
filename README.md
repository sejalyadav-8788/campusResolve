# CampusResolve

CampusResolve is a secure, deadline-driven campus grievance management platform designed to help students report and track campus-related issues while ensuring that complaints reach the appropriate authority.

## Problem Statement

Students often face difficulties when reporting campus issues because complaints may not reach the correct authority, sensitive complaints may lack privacy, and unresolved complaints can remain pending without proper escalation.

CampusResolve provides a structured digital system for complaint submission, automatic routing, deadline tracking, escalation, and resolution.

## Key Features

- Student complaint submission
- JWT-based authentication
- Role-based access control
- Automatic complaint routing
- Sensitive complaint routing to senior authorities
- Complaint status tracking
- Deadline-based automatic escalation
- Authority dashboard for assigned complaints
- Admin dashboard for monitoring complaints
- Complaint priority and category management
- PostgreSQL database for persistent data storage
- Responsive web interface
- Separate dashboards for Student, Authority, and Admin

## Complaint Workflow

Student submits complaint

        ↓

Automatic routing to appropriate authority

        ↓

Authority reviews complaint

        ↓

IN_PROGRESS

        ↓

RESOLVED

If the complaint exceeds its deadline:

        ↓

Automatic escalation

        ↓

Senior Authority

        ↓

Admin / College Head

## User Roles

### Student
- Submit complaints
- View submitted complaints
- Track complaint status
- View deadlines

### Faculty / HOD
- View assigned complaints
- Start working on complaints
- Mark complaints as resolved

### Dean
- Handle sensitive complaints
- Handle escalated complaints
- Resolve assigned complaints

### Admin
- View all complaints
- Monitor complaint statistics
- Track open, assigned, escalated, and resolved complaints

## Technology Stack

### Backend
- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate

### Database
- PostgreSQL

### Frontend
- HTML
- CSS
- JavaScript

### Development Tools
- IntelliJ IDEA
- Visual Studio Code
- Postman
- Git
- GitHub

## Project Structure

```text
campusResolve/
│
├── frontend/
│   ├── admin/
│   ├── authority/
│   └── student/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/campusResolve/backend/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── repository/
│       │       ├── security/
│       │       └── service/
│       │
│       └── resources/
│
├── pom.xml
└── README.md
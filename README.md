# HealthDesk - Hospital Management System

HealthDesk is a desktop-based Hospital Management System built in Java. The project simulates core healthcare operations, including patient registration, doctor management, multithreaded appointment processing, persistent database storage, and external REST API integration.

## Tech Stack
* Language: Java 21 (JDK Temurin 21)
* Build Tool: Maven
* UI Framework: JavaFX
* Database: SQLite JDBC
* JSON Parser: Jackson Databind
* Version Control: Git & GitHub

## Project Architecture
* com.healthdesk.app - Application entry points and system launchers
* com.healthdesk.models - Core domain entities (Person, Patient, Doctor)
* com.healthdesk.service - Business logic and dynamic data handling
* com.healthdesk.exceptions - Custom error handling classes
* com.healthdesk.util - File handling and utility operations
* com.healthdesk.ui - JavaFX interface controllers and FXML layouts
* com.healthdesk.database - SQLite connection and Data Access Objects (DAO)
* com.healthdesk.threads - Multithreading and concurrent queue logic
* com.healthdesk.api - External REST API handlers

## Key Features
* Patient Management: Registration, profile tracking, and ID-based search.
* Doctor Scheduling: Doctor profile management, specialization tracking, and availability status.
* Data Persistence: Permanent storage using file systems and relational database (SQLite).
* Graphical User Interface: Interactive JavaFX dashboard and data tables.
* Multithreaded Processing: Real-time patient queue processing without UI freezing.
* REST API Integration: Live medicine data fetching using external APIs.


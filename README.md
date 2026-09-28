# HealthDesk — Hospital Management System

**HealthDesk** is a desktop-based Hospital Management System (HMS) built with **JavaFX** and **SQLite**. It provides an intuitive, real-time dashboard to streamline hospital workflows—from patient registration and bed allocation to multithreaded emergency queue monitoring and dynamic REST API-powered drug information lookup.

---

## Key Features

* **Dashboard Overview:** Live summary of total patients, today's appointments, available doctors, revenue, occupied beds, and daily health tips.
* **Patient & Doctor Management:** Patient profiling, medical history tracking, doctor scheduling, and specialization filtering.
* **Appointments & Bed Allocation:** Real-time appointment scheduling and inpatient bed assignment monitoring.
* **Emergency Monitoring (Multithreaded):** Asynchronous background thread to manage emergency queues and bed availability without blocking the UI.
* **Lab Services & Billing:** Lab test orders, diagnostic status tracking, and automated bill generation.
* **Dynamic Drug Lookup:** Dynamic integration with **OpenFDA** & **RxNav APIs** along with local BD brand mapping (e.g., Napa, Sergel, Seclo) to fetch real-time usage, dosage, and side effects.

---

## Tech Stack

* **Language:** Java 21 (Temurin)
* **UI Framework:** JavaFX (FXML & Custom CSS styling)
* **Database:** SQLite (via SQLite JDBC & Data Access Objects / DAO)
* **Build Tool:** Apache Maven
* **JSON Processing:** org.json
* **REST APIs:** OpenFDA & NLM RxNav APIs

---



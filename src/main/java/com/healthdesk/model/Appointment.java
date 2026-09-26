package com.healthdesk.model;

import javafx.beans.property.*;

public class Appointment {
    private final IntegerProperty id              = new SimpleIntegerProperty();
    private final IntegerProperty patientId       = new SimpleIntegerProperty();
    private final StringProperty  patientName     = new SimpleStringProperty();
    private final IntegerProperty doctorId        = new SimpleIntegerProperty();
    private final StringProperty  doctorName      = new SimpleStringProperty();
    private final StringProperty  appointmentDate = new SimpleStringProperty();
    private final StringProperty  timeSlot        = new SimpleStringProperty();
    private final StringProperty  reason          = new SimpleStringProperty();
    private final StringProperty  status          = new SimpleStringProperty("SCHEDULED");
    private final StringProperty  notes           = new SimpleStringProperty();
    private final StringProperty  createdAt       = new SimpleStringProperty();

    public int    getId()                  { return id.get(); }
    public void   setId(int v)             { id.set(v); }
    public IntegerProperty idProperty()    { return id; }

    public int    getPatientId()           { return patientId.get(); }
    public void   setPatientId(int v)      { patientId.set(v); }

    public String getPatientName()         { return patientName.get(); }
    public void   setPatientName(String v) { patientName.set(v); }
    public StringProperty patientNameProperty() { return patientName; }

    public int    getDoctorId()            { return doctorId.get(); }
    public void   setDoctorId(int v)       { doctorId.set(v); }

    public String getDoctorName()          { return doctorName.get(); }
    public void   setDoctorName(String v)  { doctorName.set(v); }
    public StringProperty doctorNameProperty() { return doctorName; }

    public String getAppointmentDate()     { return appointmentDate.get(); }
    public void   setAppointmentDate(String v) { appointmentDate.set(v); }
    public StringProperty appointmentDateProperty() { return appointmentDate; }

    public String getTimeSlot()            { return timeSlot.get(); }
    public void   setTimeSlot(String v)    { timeSlot.set(v); }
    public StringProperty timeSlotProperty() { return timeSlot; }

    public String getReason()              { return reason.get(); }
    public void   setReason(String v)      { reason.set(v); }
    public StringProperty reasonProperty() { return reason; }

    public String getStatus()              { return status.get(); }
    public void   setStatus(String v)      { status.set(v); }
    public StringProperty statusProperty() { return status; }

    public String getNotes()               { return notes.get(); }
    public void   setNotes(String v)       { notes.set(v); }

    public String getCreatedAt()           { return createdAt.get(); }
    public void   setCreatedAt(String v)   { createdAt.set(v); }
}

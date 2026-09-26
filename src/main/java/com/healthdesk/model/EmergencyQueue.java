package com.healthdesk.model;

import javafx.beans.property.*;

public class EmergencyQueue {
    private final IntegerProperty id          = new SimpleIntegerProperty();
    private final IntegerProperty patientId   = new SimpleIntegerProperty();
    private final StringProperty  patientName = new SimpleStringProperty();
    private final IntegerProperty priority    = new SimpleIntegerProperty(3);
    private final StringProperty  description = new SimpleStringProperty();
    private final StringProperty  status      = new SimpleStringProperty("WAITING");
    private final StringProperty  arrivedAt   = new SimpleStringProperty();
    private final StringProperty  attendedAt  = new SimpleStringProperty();

    // Priority labels: 1 = Critical, 2 = Urgent, 3 = Normal
    public static String priorityLabel(int p) {
        return switch (p) {
            case 1 -> " Critical";
            case 2 -> " Urgent";
            default -> " Normal";
        };
    }

    public int    getId()              { return id.get(); }
    public void   setId(int v)         { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    public int    getPatientId()       { return patientId.get(); }
    public void   setPatientId(int v)  { patientId.set(v); }

    public String getPatientName()     { return patientName.get(); }
    public void   setPatientName(String v) { patientName.set(v); }
    public StringProperty patientNameProperty() { return patientName; }

    public int    getPriority()        { return priority.get(); }
    public void   setPriority(int v)   { priority.set(v); }
    public StringProperty priorityProperty() {
        return new SimpleStringProperty(priorityLabel(priority.get()));
    }

    public String getDescription()     { return description.get(); }
    public void   setDescription(String v) { description.set(v); }
    public StringProperty descriptionProperty() { return description; }

    public String getStatus()          { return status.get(); }
    public void   setStatus(String v)  { status.set(v); }
    public StringProperty statusProperty() { return status; }

    public String getArrivedAt()       { return arrivedAt.get(); }
    public void   setArrivedAt(String v) { arrivedAt.set(v); }
    public StringProperty arrivedAtProperty() { return arrivedAt; }

    public String getAttendedAt()      { return attendedAt.get(); }
    public void   setAttendedAt(String v) { attendedAt.set(v); }
}

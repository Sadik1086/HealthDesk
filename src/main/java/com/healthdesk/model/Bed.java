package com.healthdesk.model;

import javafx.beans.property.*;

public class Bed {
    private final IntegerProperty id          = new SimpleIntegerProperty();
    private final StringProperty  bedNumber   = new SimpleStringProperty();
    private final StringProperty  ward        = new SimpleStringProperty();
    private final IntegerProperty patientId   = new SimpleIntegerProperty(0);
    private final StringProperty  patientName = new SimpleStringProperty();
    private final StringProperty  status      = new SimpleStringProperty("AVAILABLE");
    private final StringProperty  admittedAt  = new SimpleStringProperty();

    public int    getId()              { return id.get(); }
    public void   setId(int v)         { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    public String getBedNumber()       { return bedNumber.get(); }
    public void   setBedNumber(String v) { bedNumber.set(v); }
    public StringProperty bedNumberProperty() { return bedNumber; }

    public String getWard()            { return ward.get(); }
    public void   setWard(String v)    { ward.set(v); }
    public StringProperty wardProperty() { return ward; }

    public int    getPatientId()       { return patientId.get(); }
    public void   setPatientId(int v)  { patientId.set(v); }

    public String getPatientName()     { return patientName.get(); }
    public void   setPatientName(String v) { patientName.set(v); }
    public StringProperty patientNameProperty() { return patientName; }

    public String getStatus()          { return status.get(); }
    public void   setStatus(String v)  { status.set(v); }
    public StringProperty statusProperty() { return status; }

    public String getAdmittedAt()      { return admittedAt.get(); }
    public void   setAdmittedAt(String v) { admittedAt.set(v); }
    public StringProperty admittedAtProperty() { return admittedAt; }
}

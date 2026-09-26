package com.healthdesk.model;

import javafx.beans.property.*;

public class LabOrder {
    private final IntegerProperty id          = new SimpleIntegerProperty();
    private final IntegerProperty patientId   = new SimpleIntegerProperty();
    private final StringProperty  patientName = new SimpleStringProperty();
    private final IntegerProperty testId      = new SimpleIntegerProperty();
    private final StringProperty  testName    = new SimpleStringProperty();
    private final IntegerProperty doctorId    = new SimpleIntegerProperty();
    private final StringProperty  doctorName  = new SimpleStringProperty();
    private final StringProperty  orderedAt   = new SimpleStringProperty();
    private final StringProperty  result      = new SimpleStringProperty();
    private final StringProperty  resultDate  = new SimpleStringProperty();
    private final StringProperty  status      = new SimpleStringProperty("PENDING");

    public int    getId()              { return id.get(); }
    public void   setId(int v)         { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    public int    getPatientId()       { return patientId.get(); }
    public void   setPatientId(int v)  { patientId.set(v); }

    public String getPatientName()     { return patientName.get(); }
    public void   setPatientName(String v) { patientName.set(v); }
    public StringProperty patientNameProperty() { return patientName; }

    public int    getTestId()          { return testId.get(); }
    public void   setTestId(int v)     { testId.set(v); }

    public String getTestName()        { return testName.get(); }
    public void   setTestName(String v) { testName.set(v); }
    public StringProperty testNameProperty() { return testName; }

    public int    getDoctorId()        { return doctorId.get(); }
    public void   setDoctorId(int v)   { doctorId.set(v); }

    public String getDoctorName()      { return doctorName.get(); }
    public void   setDoctorName(String v) { doctorName.set(v); }
    public StringProperty doctorNameProperty() { return doctorName; }

    public String getOrderedAt()       { return orderedAt.get(); }
    public void   setOrderedAt(String v) { orderedAt.set(v); }
    public StringProperty orderedAtProperty() { return orderedAt; }

    public String getResult()          { return result.get(); }
    public void   setResult(String v)  { result.set(v); }

    public String getResultDate()      { return resultDate.get(); }
    public void   setResultDate(String v) { resultDate.set(v); }

    public String getStatus()          { return status.get(); }
    public void   setStatus(String v)  { status.set(v); }
    public StringProperty statusProperty() { return status; }
}

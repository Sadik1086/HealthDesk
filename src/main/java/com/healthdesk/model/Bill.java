package com.healthdesk.model;

import javafx.beans.property.*;

public class Bill {
    private final IntegerProperty id               = new SimpleIntegerProperty();
    private final IntegerProperty patientId        = new SimpleIntegerProperty();
    private final StringProperty  patientName      = new SimpleStringProperty();
    private final DoubleProperty  consultationFee  = new SimpleDoubleProperty();
    private final DoubleProperty  labCharges       = new SimpleDoubleProperty();
    private final DoubleProperty  bedCharges       = new SimpleDoubleProperty();
    private final DoubleProperty  medicineCharges  = new SimpleDoubleProperty();
    private final DoubleProperty  total            = new SimpleDoubleProperty();
    private final StringProperty  paymentStatus    = new SimpleStringProperty("UNPAID");
    private final StringProperty  paymentMethod    = new SimpleStringProperty();
    private final StringProperty  createdAt        = new SimpleStringProperty();

    public int    getId()                    { return id.get(); }
    public void   setId(int v)               { id.set(v); }
    public IntegerProperty idProperty()      { return id; }

    public int    getPatientId()             { return patientId.get(); }
    public void   setPatientId(int v)        { patientId.set(v); }

    public String getPatientName()           { return patientName.get(); }
    public void   setPatientName(String v)   { patientName.set(v); }
    public StringProperty patientNameProperty() { return patientName; }

    public double getConsultationFee()       { return consultationFee.get(); }
    public void   setConsultationFee(double v) { consultationFee.set(v); }
    public DoubleProperty consultationFeeProperty() { return consultationFee; }

    public double getLabCharges()            { return labCharges.get(); }
    public void   setLabCharges(double v)    { labCharges.set(v); }
    public DoubleProperty labChargesProperty() { return labCharges; }

    public double getBedCharges()            { return bedCharges.get(); }
    public void   setBedCharges(double v)    { bedCharges.set(v); }
    public DoubleProperty bedChargesProperty() { return bedCharges; }

    public double getMedicineCharges()       { return medicineCharges.get(); }
    public void   setMedicineCharges(double v) { medicineCharges.set(v); }
    public DoubleProperty medicineChargesProperty() { return medicineCharges; }

    public double getTotal()                 { return total.get(); }
    public void   setTotal(double v)         { total.set(v); }
    public DoubleProperty totalProperty()    { return total; }

    public String getPaymentStatus()         { return paymentStatus.get(); }
    public void   setPaymentStatus(String v) { paymentStatus.set(v); }
    public StringProperty paymentStatusProperty() { return paymentStatus; }

    public String getPaymentMethod()         { return paymentMethod.get(); }
    public void   setPaymentMethod(String v) { paymentMethod.set(v); }

    public String getCreatedAt()             { return createdAt.get(); }
    public void   setCreatedAt(String v)     { createdAt.set(v); }
    public StringProperty createdAtProperty() { return createdAt; }
}

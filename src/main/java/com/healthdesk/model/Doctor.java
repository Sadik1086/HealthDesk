package com.healthdesk.model;

import javafx.beans.property.*;

public class Doctor {
    private final IntegerProperty id             = new SimpleIntegerProperty();
    private final StringProperty  name           = new SimpleStringProperty();
    private final StringProperty  specialization = new SimpleStringProperty();
    private final StringProperty  phone          = new SimpleStringProperty();
    private final StringProperty  email          = new SimpleStringProperty();
    private final IntegerProperty departmentId   = new SimpleIntegerProperty();
    private final StringProperty  departmentName = new SimpleStringProperty();
    private final BooleanProperty available      = new SimpleBooleanProperty(true);

    public int    getId()                { return id.get(); }
    public void   setId(int v)           { id.set(v); }
    public IntegerProperty idProperty()  { return id; }

    public String getName()              { return name.get(); }
    public void   setName(String v)      { name.set(v); }
    public StringProperty nameProperty() { return name; }

    public String getSpecialization()    { return specialization.get(); }
    public void   setSpecialization(String v) { specialization.set(v); }
    public StringProperty specializationProperty() { return specialization; }

    public String getPhone()             { return phone.get(); }
    public void   setPhone(String v)     { phone.set(v); }
    public StringProperty phoneProperty() { return phone; }

    public String getEmail()             { return email.get(); }
    public void   setEmail(String v)     { email.set(v); }

    public int    getDepartmentId()      { return departmentId.get(); }
    public void   setDepartmentId(int v) { departmentId.set(v); }

    public String getDepartmentName()    { return departmentName.get(); }
    public void   setDepartmentName(String v) { departmentName.set(v); }
    public StringProperty departmentNameProperty() { return departmentName; }

    public boolean isAvailable()         { return available.get(); }
    public void    setAvailable(boolean v) { available.set(v); }
    public StringProperty availableProperty() {
        return new SimpleStringProperty(available.get() ? "Available" : "Unavailable");
    }

    @Override public String toString() { return name.get(); }
}

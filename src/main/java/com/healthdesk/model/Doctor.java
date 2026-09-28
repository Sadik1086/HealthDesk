package com.healthdesk.model;

import javafx.beans.property.*;

public class Doctor extends Person{
    private final StringProperty  specialization = new SimpleStringProperty();
    private final IntegerProperty departmentId   = new SimpleIntegerProperty();
    private final StringProperty  departmentName = new SimpleStringProperty();
    private final BooleanProperty available      = new SimpleBooleanProperty(true);

    public String getSpecialization()    { return specialization.get(); }
    public void   setSpecialization(String v) { specialization.set(v); }
    public StringProperty specializationProperty() { return specialization; }
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
    @Override
    public String getRole() { return "DOCTOR"; }
}

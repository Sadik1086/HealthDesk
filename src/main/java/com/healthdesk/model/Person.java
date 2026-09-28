package com.healthdesk.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Person {
    protected final IntegerProperty id    = new SimpleIntegerProperty();
    protected final StringProperty name  = new SimpleStringProperty();
    protected final StringProperty  phone = new SimpleStringProperty();
    protected final StringProperty  email = new SimpleStringProperty();
    public int getId() { return id.get(); }
    public void setId(int v) { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String v) { name.set(v); }
    public StringProperty nameProperty() { return name; }

    public String getPhone() { return phone.get(); }
    public void setPhone(String v) { phone.set(v); }
    public StringProperty phoneProperty() { return phone; }

    public String getEmail() { return email.get(); }
    public void setEmail(String v) { email.set(v); }
    public StringProperty emailProperty() { return email; }
    public abstract String getRole();
    @Override
    public String toString() { return name.get(); }
}
package com.healthdesk.model;

import javafx.beans.property.*;

public class Patient {
    private final IntegerProperty id           = new SimpleIntegerProperty();
    private final StringProperty  name         = new SimpleStringProperty();
    private final IntegerProperty age          = new SimpleIntegerProperty();
    private final StringProperty  gender       = new SimpleStringProperty();
    private final StringProperty  phone        = new SimpleStringProperty();
    private final StringProperty  email        = new SimpleStringProperty();
    private final StringProperty  address      = new SimpleStringProperty();
    private final StringProperty  bloodGroup   = new SimpleStringProperty();
    private final StringProperty  registeredAt = new SimpleStringProperty();

    public int    getId()              { return id.get(); }
    public void   setId(int v)         { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    public String getName()            { return name.get(); }
    public void   setName(String v)    { name.set(v); }
    public StringProperty nameProperty() { return name; }

    public int    getAge()             { return age.get(); }
    public void   setAge(int v)        { age.set(v); }
    public IntegerProperty ageProperty() { return age; }

    public String getGender()          { return gender.get(); }
    public void   setGender(String v)  { gender.set(v); }
    public StringProperty genderProperty() { return gender; }

    public String getPhone()           { return phone.get(); }
    public void   setPhone(String v)   { phone.set(v); }
    public StringProperty phoneProperty() { return phone; }

    public String getEmail()           { return email.get(); }
    public void   setEmail(String v)   { email.set(v); }
    public StringProperty emailProperty() { return email; }

    public String getAddress()         { return address.get(); }
    public void   setAddress(String v) { address.set(v); }
    public StringProperty addressProperty() { return address; }

    public String getBloodGroup()      { return bloodGroup.get(); }
    public void   setBloodGroup(String v) { bloodGroup.set(v); }
    public StringProperty bloodGroupProperty() { return bloodGroup; }

    public String getRegisteredAt()    { return registeredAt.get(); }
    public void   setRegisteredAt(String v) { registeredAt.set(v); }
    public StringProperty registeredAtProperty() { return registeredAt; }
}

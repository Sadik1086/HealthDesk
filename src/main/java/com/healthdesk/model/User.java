package com.healthdesk.model;

import javafx.beans.property.*;

public class User {
    private final IntegerProperty id       = new SimpleIntegerProperty();
    private final StringProperty username  = new SimpleStringProperty();
    private final StringProperty fullName  = new SimpleStringProperty();
    private final StringProperty role      = new SimpleStringProperty();
    private final BooleanProperty active   = new SimpleBooleanProperty(true);

    public int     getId()          { return id.get(); }
    public void    setId(int v)     { id.set(v); }

    public String  getUsername()    { return username.get(); }
    public void    setUsername(String v) { username.set(v); }

    public String  getFullName()    { return fullName.get(); }
    public void    setFullName(String v) { fullName.set(v); }

    public String  getRole()        { return role.get(); }
    public void    setRole(String v) { role.set(v); }

    public boolean isActive()       { return active.get(); }
    public void    setActive(boolean v) { active.set(v); }
}

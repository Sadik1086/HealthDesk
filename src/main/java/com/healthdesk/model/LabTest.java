package com.healthdesk.model;

import javafx.beans.property.*;

public class LabTest {
    private final IntegerProperty id          = new SimpleIntegerProperty();
    private final StringProperty  name        = new SimpleStringProperty();
    private final StringProperty  description = new SimpleStringProperty();
    private final DoubleProperty  price       = new SimpleDoubleProperty();

    public int    getId()            { return id.get(); }
    public void   setId(int v)       { id.set(v); }

    public String getName()          { return name.get(); }
    public void   setName(String v)  { name.set(v); }
    public StringProperty nameProperty() { return name; }

    public String getDescription()   { return description.get(); }
    public void   setDescription(String v) { description.set(v); }
    public StringProperty descriptionProperty() { return description; }

    public double getPrice()         { return price.get(); }
    public void   setPrice(double v) { price.set(v); }
    public DoubleProperty priceProperty() { return price; }

    @Override public String toString() { return name.get(); }
}

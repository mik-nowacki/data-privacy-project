package pt.unl.fct.pds.proj1server.model;

import jakarta.persistence.*;

public class CountResponse {
    private String attribute;
    private double value;
    private double remaining;
    private double sensitivity;

    public CountResponse() {
    }

    public CountResponse(
            String attribute,
            double value,
            double remaining,
            double sensitivity) {
        this.attribute = attribute;
        this.value = value;
        this.remaining = remaining;
        this.sensitivity = sensitivity;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public double getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }
}

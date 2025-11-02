package pt.unl.fct.pds.proj1server.model;

import jakarta.persistence.*;

public class HistographRequest {
    private String value;

    public HistographRequest() {
    }

    public HistographRequest(
            String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

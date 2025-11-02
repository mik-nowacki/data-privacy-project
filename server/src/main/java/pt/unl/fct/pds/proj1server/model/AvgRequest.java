package pt.unl.fct.pds.proj1server.model;

import jakarta.persistence.*;

public class AvgRequest {
    private String attribute;
    private String value;

    public AvgRequest() {
    }

    public AvgRequest(
            String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}

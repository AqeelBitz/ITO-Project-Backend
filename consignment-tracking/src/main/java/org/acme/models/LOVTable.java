package org.acme.models;

public class LOVTable {
    public Long lov_id;
    public String lov_value;

    public LOVTable(Long lov_id, String lov_value) {
        this.lov_id = lov_id;
        this.lov_value = lov_value;
    }

    public Long getlov_id() {
        return lov_id;
    }

    public void setlov_id(Long lov_id) {
        this.lov_id = lov_id;
    }

    public String getlov_value() {
        return lov_value;
    }

    public void setlov_value(String lov_value) {
        this.lov_value = lov_value;
    }
}

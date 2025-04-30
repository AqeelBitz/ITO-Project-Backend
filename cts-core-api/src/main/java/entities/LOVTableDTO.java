package entities;

public class LOVTableDTO {
    public Integer lov_id;
    public String lov_value;

    public LOVTableDTO(Integer lov_id, String lov_value) {
        this.lov_id = lov_id;
        this.lov_value = lov_value;
    }

    public Integer getlov_id() {
        return lov_id;
    }

    public void setlov_id(Integer lov_id) {
        this.lov_id = lov_id;
    }

    public String getlov_value() {
        return lov_value;
    }

    public void setlov_value(String lov_value) {
        this.lov_value = lov_value;
    }
}

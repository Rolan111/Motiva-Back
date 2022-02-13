package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;

@Document(collectionName = "city")
public class CityEntity extends AbstractEntity {
    private Integer idCity;
    private String name;
    private String state;

    @PropertyName("id_city")
    public Integer getIdCity() {
        return idCity;
    }

    @PropertyName("id_city")
    public void setIdCity(Integer idCity) {
        this.idCity = idCity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

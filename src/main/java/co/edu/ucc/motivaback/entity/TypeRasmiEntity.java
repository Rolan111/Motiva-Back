package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;

/**
 * @author nagredo
 * @project motiva-back
 * @class AlertEntity
 */
@Document(collectionName = "type_rasmi")
public class TypeRasmiEntity extends AbstractEntity {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

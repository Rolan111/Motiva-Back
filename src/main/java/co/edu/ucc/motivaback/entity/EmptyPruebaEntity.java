package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;

@Document(collectionName = "answer")

public class EmptyPruebaEntity {

    private String openAnswer;

    @PropertyName("open_answer")
    public String getOpenAnswer() {
        return openAnswer;
    }

    @PropertyName("open_answer")
    public void setOpenAnswer(String openAnswer) {
        this.openAnswer = openAnswer;
    }
}

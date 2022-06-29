package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;

@Document(collectionName = "option_answer")
public class OptionAnswerEntity extends AbstractEntity {

    private Long idOptionAnswer;
    private Long idQuestion;
    private String description;
    private Long order;
    private String type;

    @PropertyName("id_option_answer")
    public Long getIdOptionAnswer() {
        return idOptionAnswer;
    }

    @PropertyName("id_option_answer")
    public void setIdOptionAnswer(Long idOptionAnswer) {
        this.idOptionAnswer = idOptionAnswer;
    }

    @PropertyName("id_question")
    public Long getIdQuestion() {
        return idQuestion;
    }

    @PropertyName("id_question")
    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

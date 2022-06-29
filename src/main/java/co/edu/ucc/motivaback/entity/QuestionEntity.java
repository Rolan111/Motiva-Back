package co.edu.ucc.motivaback.entity;

import co.edu.ucc.motivaback.enums.QuestionTypeEnum;
import co.edu.ucc.motivaback.enums.TypeQuestionaryEnum;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuestionDto
 */
@Document(collectionName = "question")
public class QuestionEntity extends AbstractEntity {

    private Long idQuestion;
    private String description;
    private Long idFather;
    private Long order;
    private QuestionTypeEnum questionaryType;
    private TypeQuestionaryEnum type;


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

    @PropertyName("id_father")
    public Long getIdFather() {
        return idFather;
    }

    @PropertyName("id_father")
    public void setIdFather(Long idFather) {
        this.idFather = idFather;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public TypeQuestionaryEnum getType() {
        return type;
    }

    public void setType(TypeQuestionaryEnum type) {
        this.type = type;
    }

    @PropertyName("questionary_type")
    public QuestionTypeEnum getQuestionaryType() {
        return questionaryType;
    }

    @PropertyName("questionary_type")
    public void setQuestionaryType(QuestionTypeEnum questionaryType) {
        this.questionaryType = questionaryType;
    }
}

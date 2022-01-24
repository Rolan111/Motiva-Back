package co.edu.ucc.motivaback.dto;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuestionDto
 */
public class QuestionDto {
    private Integer id_question;
    private String description;
    private Integer id_father;
    private Integer id_question_type;
    private Integer order;

    public Integer getId_question() {
        return id_question;
    }

    public void setId_question(Integer id_question) {
        this.id_question = id_question;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId_father() {
        return id_father;
    }

    public void setId_father(Integer id_father) {
        this.id_father = id_father;
    }

    public Integer getId_question_type() {
        return id_question_type;
    }

    public void setId_question_type(Integer id_question_type) {
        this.id_question_type = id_question_type;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}

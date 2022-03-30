package co.edu.ucc.motivaback.entity;

public class CareSheetOptionAnwerEntity extends AbstractEntity{

    public int id_option_answer;
    public int id_question;
    public String description;

    public int getId_option_answer() {
        return id_option_answer;
    }

    public void setId_option_answer(int id_option_answer) {
        this.id_option_answer = id_option_answer;
    }

    public int getId_question() {
        return id_question;
    }

    public void setId_question(int id_question) {
        this.id_question = id_question;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

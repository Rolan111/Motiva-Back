package co.edu.ucc.motivaback.entity;

import java.util.List;

public class CareSheetAnswerEntity extends AbstractEntity {

    private int id_question;
    private List<Integer> id_option_answers;
    private String open_answer;
    private int id_poll;

    public int getId_question() {
        return id_question;
    }

    public void setId_question(int id_question) {
        this.id_question = id_question;
    }

    public List<Integer> getId_option_answers() {
        return id_option_answers;
    }

    public void setId_option_answers(List<Integer> id_option_answers) {
        this.id_option_answers = id_option_answers;
    }

    public String getOpen_answer() {
        return open_answer;
    }

    public void setOpen_answer(String open_answer) {
        this.open_answer = open_answer;
    }

    public int getId_poll() {
        return id_poll;
    }

    public void setId_poll(int id_poll) {
        this.id_poll = id_poll;
    }
}

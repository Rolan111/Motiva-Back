package co.edu.ucc.motivaback.entity;

public class CareSheetAnswerPsychosocial extends AbstractEntity {

    private Integer id_question;
    private Integer id_option_answer;
    private String open_answer;
    private Integer id_poll;

    public Integer getId_question() {
        return id_question;
    }

    public void setId_question(Integer id_question) {
        this.id_question = id_question;
    }

    public Integer getId_option_answer() {
        return id_option_answer;
    }

    public void setId_option_answer(Integer id_option_answer) {
        this.id_option_answer = id_option_answer;
    }

    public String getOpen_answer() {
        return open_answer;
    }

    public void setOpen_answer(String open_answer) {
        this.open_answer = open_answer;
    }

    public Integer getId_poll() {
        return id_poll;
    }

    public void setId_poll(Integer id_poll) {
        this.id_poll = id_poll;
    }
}

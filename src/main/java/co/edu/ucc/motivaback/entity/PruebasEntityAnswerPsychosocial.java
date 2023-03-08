package co.edu.ucc.motivaback.entity;

public class PruebasEntityAnswerPsychosocial {

    private String id_poll;
    private Integer id_question;
    private Integer id_option_answer;
    private String open_answer;
    private String createdAt2;

    public String getCreatedAt2() {
        return createdAt2;
    }

    public void setCreatedAt2(String createdAt2) {
        this.createdAt2 = createdAt2;
    }

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

    public String getId_poll() {
        return id_poll;
    }

    public void setId_poll(String id_poll) {
        this.id_poll = id_poll;
    }

    public PruebasEntityAnswerPsychosocial(String id_poll, Integer id_question, Integer id_option_answer, String open_answer, String createdAt2) {
        this.id_poll = id_poll;
        this.id_question = id_question;
        this.id_option_answer = id_option_answer;
        this.open_answer = open_answer;
        this.createdAt2 = createdAt2;
    }

    public PruebasEntityAnswerPsychosocial() {
    }
}

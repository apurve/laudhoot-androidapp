package com.laudhoot.web.model;

/**
 * Created by root on 31/1/16.
 */
public class FAQTO extends BaseTO {

    String question;

    String answer;

    public FAQTO() {
        super();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}

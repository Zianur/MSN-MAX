package com.zerotwotalkingapp;

import com.google.firebase.database.Exclude;

public class ConversationModule {

    private String question;

    public ConversationModule() {


    }

    public ConversationModule(String question) {
        this.question = question;

    }



    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

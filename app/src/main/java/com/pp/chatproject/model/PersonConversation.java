package com.pp.chatproject.model;


import com.pp.chatproject.service.PersonService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Created by leonardofoliveira on 30/10/2017.
 */

public class PersonConversation  extends Model {

    private String conversationId;

    private String title;// reciever name

    private String info;// last message + last message date


    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public PersonConversation(){}
    public PersonConversation(String conversationId, String title, String info){
        this.conversationId = conversationId;
        this.title = title;
        this.info = info;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}

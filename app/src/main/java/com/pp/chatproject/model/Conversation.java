package com.pp.chatproject.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardofoliveira on 30/10/2017.
 */

public class Conversation extends Model implements Comparable<Conversation> {

    public static final String SIMPLE_TYPE = "SIMPLE";

    private String id;

    private String init;

    private String type;

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;


    public Conversation(){
        setInitAsLocalDate(this, LocalDate.now());
    }

    public Conversation(String id, String type){
        this.id = id;
        this.type = type;
        setInitAsLocalDate(this, LocalDate.now());
    }

    public static LocalDate getInitAsLocalDate(Conversation conversation) {
        if(conversation == null || conversation.getInit() == null){return LocalDate.now();}
        return LocalDate.parse(conversation.getInit(), formatter);
    }

    public static void setInitAsLocalDate(Conversation conversation, LocalDate init) {
        if(conversation == null){return;}
        if(init == null){ init = LocalDate.now();}
        conversation.init = init.format(formatter);
    }


    @Override
    public int compareTo(@NonNull Conversation conversation) {
        LocalDate date = getInitAsLocalDate(this);
        return date.compareTo(getInitAsLocalDate(conversation));
    }

    //get set
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }
}

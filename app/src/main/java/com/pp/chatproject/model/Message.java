package com.pp.chatproject.model;

import android.support.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by leonardofoliveira on 30/10/2017.
 */

public class Message extends Model implements Comparable<Message>{
    
    private String id;
    
    private String senderId;
    
    private String info; //senderDisplayName;
    
    private String message;
    
    private String dateTime;

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Message(){
        setDateTimeAsLocalDateTime(this, LocalDateTime.now());
    }

    public static LocalDateTime getDateTimeAsLocalDateTime(Message message) {
        if(message == null || message.getDateTime() == null){
            return LocalDateTime.now();
        }
        return LocalDateTime.parse(message.getDateTime(), formatter);}

    public static void setDateTimeAsLocalDateTime(Message message, LocalDateTime dateTime) {
        if(message == null){return;}
        if(dateTime == null){ dateTime = LocalDateTime.now();}
        message.dateTime = dateTime.format(formatter);
    }

    @Override
    public int compareTo(@NonNull Message message) {
        LocalDateTime dateTime = getDateTimeAsLocalDateTime(this);
        return dateTime.compareTo(getDateTimeAsLocalDateTime(message));
    }

    // get set
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}

package com.pp.chatproject.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by leonardofoliveira on 30/10/2017.
 */

public class Person extends Model implements Comparable<Person>{

    
    private String id;

    @Exclude
    private List<String> conversations = new ArrayList<>();

    
    private String displayName;

    
    private String email;

    
    private String phoneNumber;

    
    private String photoUrl;

    
    private String providerId;

    
    private String uid;



    @Override
    public int compareTo(@NonNull Person person) {
        if(getUid() == null){
            return getId().compareTo(person.getId());
        }
        return getUid().compareTo(person.getUid());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getConversations() {
        if(conversations == null) {
            return  Collections.emptyList();
        }
        return conversations;
    }

    public void setConversations(List<String> conversations) {
        this.conversations = conversations;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}

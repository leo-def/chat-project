package com.pp.chatproject.service;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pp.chatproject.model.Conversation;
import com.pp.chatproject.model.Message;
import com.pp.chatproject.model.Person;
import com.pp.chatproject.model.PersonConversation;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by leonardofoliveira on 01/11/2017.
 */

public class ConversationService {

    private DatabaseReference conversationDatabaseReference;
    private DatabaseReference personConversationReference;
    private DatabaseReference friendsReference;

    public ConversationService(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        personConversationReference = databaseReference.child("person_conversation");
        conversationDatabaseReference = databaseReference.child("conversation");
    }

    public void find(final String id,@NotNull final Consumer<Conversation> consumer){

        conversationDatabaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Conversation conversation = dataSnapshot.getValue(Conversation.class);

                consumer.accept(conversation);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void create(final String type, final Consumer<Conversation> consumer){
        Conversation conversation = new Conversation();
        DatabaseReference newConversationReference = conversationDatabaseReference.push();

        conversation.setId(newConversationReference.getKey());
        conversation.setType(type);
        Conversation.setInitAsLocalDate(conversation, LocalDate.now());
        newConversationReference.setValue(conversation);
        consumer.accept(conversation);

    }
}

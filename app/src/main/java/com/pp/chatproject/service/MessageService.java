package com.pp.chatproject.service;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pp.chatproject.model.Conversation;
import com.pp.chatproject.model.Message;
import com.pp.chatproject.model.Person;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by leonardofoliveira on 16/11/2017.
 */

public class MessageService {

    private DatabaseReference messageDatabaseReference;

    public MessageService() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        messageDatabaseReference = databaseReference.child("messages");
    }

    public void sendMessage(String messageValue, Conversation conversation, Person sender, Consumer<Message> consumer){
        sendMessage(messageValue, conversation, sender, consumer);
    }
    public void sendMessage(String messageValue, String conversationId, Person sender, Consumer<Message> consumer) {
        DatabaseReference newMessageDatabaseReference = messageDatabaseReference
                .child(conversationId)
                .push();
        Message message = new Message();
        message.setMessage(messageValue);
        message.setId(newMessageDatabaseReference.getKey());
        Message.setDateTimeAsLocalDateTime(message, LocalDateTime.now());
        message.setSenderId(sender.getId());
        message.setInfo(sender.getDisplayName());

        newMessageDatabaseReference.setValue(message);
        consumer.accept(message);
    }

    public void getConversationMessageList(final String conversationId, final Consumer<List<Message>> consumer) {
        final List<Message> list = new ArrayList<Message>();
        messageDatabaseReference
                .child(conversationId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Message message = child.getValue(Message.class);
                            Log.d("!!Message In Service", message.getMessage());
                            list.add(message);
                        }
                        if (consumer != null) {
                            consumer.accept(list);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}

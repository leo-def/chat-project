package com.pp.chatproject.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pp.chatproject.model.Conversation;
import com.pp.chatproject.model.Person;
import com.pp.chatproject.model.PersonConversation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by leonardofoliveira on 20/11/2017.
 */

public class PersonConversationService {

    private ConversationService conversationService;
    private DatabaseReference personConversationDatabaseReference;
    private static final String CHILD_SIMPLE_CONVERSATION_KEY = "SIMPLE";

    public PersonConversationService(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        personConversationDatabaseReference = databaseReference.child("person_conversation");
        conversationService = new ConversationService();
    }


    public void simpleByPerson(Person person, final Consumer<List<PersonConversation>> consumer){
        simpleByPerson(person.getId(), consumer);
    }
    public void simpleByPerson(String person, final Consumer<List<PersonConversation>> consumer){
        personConversationDatabaseReference
                .child(person)
                .child(CHILD_SIMPLE_CONVERSATION_KEY)
               .addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                        consumer.accept(dataSnapshotToPersonConversationList(dataSnapshot));
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });
    }

    public void findOrCreateSimple(final Person sender, final Person receiver, final Consumer<PersonConversation> consumer){
        find(sender, receiver, new Consumer<PersonConversation>() {
            @Override
            public void accept(PersonConversation personConversation) {
                if(personConversation == null){
                    createSimple(sender, receiver, consumer);
                }else{
                    consumer.accept(personConversation);
                }
            }
        });
    }

    public void find(final Person sender, final Person receiver, final Consumer<PersonConversation> consumer){
        personConversationDatabaseReference
                .child(sender.getId())
                .child(CHILD_SIMPLE_CONVERSATION_KEY)
                .child(receiver.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            consumer.accept(dataSnapshot.getValue(PersonConversation.class));
                        }else{
                            consumer.accept(null);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void createSimple(final Person sender, final Person receiver, final Consumer<PersonConversation> consumer){
        conversationService.create(Conversation.SIMPLE_TYPE, new Consumer<Conversation>() {
            @Override
            public void accept(Conversation conversation) {
                createForConversation(receiver, sender, conversation);
                consumer.accept(createForConversation(receiver,sender,conversation));
            }
        });

    }

    public PersonConversation createForConversation(
            Person sender,
            Person receiver,
            Conversation conversation){
        PersonConversation personConversation = new PersonConversation();
        personConversation.setInfo(conversation.getInit());
        personConversation.setConversationId(conversation.getId());
        personConversation.setTitle(receiver.getDisplayName());

        personConversationDatabaseReference
                .child(sender.getId())
                .child(CHILD_SIMPLE_CONVERSATION_KEY)
                .child(receiver.getId())
                .setValue(personConversation);
        return personConversation;
    }

    public static List<PersonConversation> dataSnapshotToPersonConversationList(DataSnapshot dataSnapshot){
        List<PersonConversation> list = new ArrayList<PersonConversation>();
        for(DataSnapshot child : dataSnapshot.getChildren()){
            PersonConversation personConversation = child.getValue(PersonConversation.class);
            list.add(personConversation);
        }
        return list;
    }
}

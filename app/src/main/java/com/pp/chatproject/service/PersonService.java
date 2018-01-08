package com.pp.chatproject.service;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pp.chatproject.model.Conversation;
import com.pp.chatproject.model.Person;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by leonardofoliveira on 01/11/2017.
 */

public class PersonService {

    private DatabaseReference personDatabaseReference;

    public PersonService(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        personDatabaseReference = databaseReference.child("people");
    }
    public void byFilter(final String filter,final int limit, final Consumer<List<Person>> consumer){
        final List<Person> list = new ArrayList<Person>();
        final AtomicInteger counter = new AtomicInteger();
        personDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()){

                    int index = counter.getAndIncrement();
                    if(index == limit){break;}
                    Person person = (child.getValue(Person.class));
                    if(filter == null || person.getDisplayName().toLowerCase().contains(filter.toLowerCase())){

                        Log.w("Finded USer", child.getValue().toString());
                        list.add(person);
                    }

                }
                if(consumer != null) {
                    consumer.accept(list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void byEmail(final String email, final Consumer<Person> consumer){
        if(email == null){
            consumer.accept(null);
            return;
        }
        personDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Person person = (child.getValue(Person.class));
                    if(email.equals(person.getEmail())){
                        consumer.accept(person);
                        return;
                    }
                }
                consumer.accept(null);
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void find(String id,@NotNull final Consumer<Person> listener){

        personDatabaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(listener != null){
                    listener.accept(dataSnapshot.getValue(Person.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void save(final Person loged,@NotNull final Consumer<Person> consumer){
        byEmail(loged.getEmail(),new Consumer<Person>(){
            @Override
            public void accept(Person person) {
                if(person == null){
                    DatabaseReference newValueReference = personDatabaseReference.push();
                    loged.setId(newValueReference.getKey());
                    newValueReference.setValue(loged);

                }
                if(consumer != null) {
                    consumer.accept(person);
                }
            }
        });

    }


}

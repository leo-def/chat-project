package com.pp.chatproject.service;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.pp.chatproject.model.Person;
import com.pp.chatproject.modelmapper.ModelMapperConfig;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created by leonardofoliveira on 30/10/2017.
 */

public class AuthService {
    private Person user;
    private static AuthService instance = new AuthService();
    private PersonService personService = new PersonService();

    public void  updateUser(final FirebaseUser user, final Consumer<Person> consumer){
        if(user == null){
            this.user= null;
            consumer.accept(null);
        }
        ModelMapperConfig.firebaseUserTOPerson(user, new Consumer<Person>() {
            @Override
            public void accept(Person person) {
                updateUser(person, consumer);
            }
        });
    }

    public void  updateUser(final Person user, final Consumer<Person> consumer){
        if(user == null){
            this.user= null;
            consumer.accept(null);
        }
            final AuthService service = this;
        final Consumer<Person> updateUserConsumer = new Consumer<Person>() {
            @Override
            public void accept(Person person) {
                Log.w("PERSON", String.valueOf(person));
                service.user = person;
                consumer.accept(service.user);
            }
        };
        updateUserConsumer.andThen(consumer);
        personService.save(user, updateUserConsumer );
    }

    public boolean isAuth(){
        return user != null;
    }

    public Person getUser(){
        return user;
    }

    public void logout(){
        user = null;
    }


    //static
    public static AuthService getInstance(){
        return instance;
    }
}

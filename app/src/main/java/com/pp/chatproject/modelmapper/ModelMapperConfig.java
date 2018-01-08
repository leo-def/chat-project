package com.pp.chatproject.modelmapper;

import com.google.firebase.auth.FirebaseUser;
import com.pp.chatproject.dto.RowPersonDTO;
import com.pp.chatproject.model.Conversation;
import com.pp.chatproject.model.Message;
import com.pp.chatproject.model.Person;
import com.pp.chatproject.model.PersonConversation;
import com.pp.chatproject.service.AuthService;
import com.pp.chatproject.service.MessageService;
import com.pp.chatproject.service.PersonService;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by leonardofoliveira on 31/10/2017.
 */

public class ModelMapperConfig {
        static final PersonService personService = new PersonService();
        static final MessageService messageService = new MessageService();

        public static void firebaseUserTOPerson(FirebaseUser firebaseUser, Consumer<Person> consumer){
            Person person = new Person();
            if(firebaseUser.getPhotoUrl() != null) {
                person.setPhotoUrl(firebaseUser.getPhotoUrl().toString());
            }
            person.setDisplayName(firebaseUser.getDisplayName());
            person.setEmail(firebaseUser.getEmail());
            person.setPhoneNumber(firebaseUser.getPhoneNumber());
            person.setProviderId(firebaseUser.getProviderId());
            person.setUid(firebaseUser.getUid());

            //objectToObject(firebaseUser, person);
            if(consumer != null) {
                consumer.accept(person);
            }
        }

        public static void personToRowPersonDTO(final Person person, Consumer<RowPersonDTO> consumer){
            RowPersonDTO dto = new RowPersonDTO();
            dto.setDisplayName(person.getDisplayName());
            if(consumer != null) {
                consumer.accept(dto);
            }
        }
        /*
        public static void objectToObject(Object source, Object target){
            for(Field sourceField : source.getClass().getDeclaredFields()){
                try {
                    sourceField.setAccessible(true);
                    Field field = target.getClass().getDeclaredField(sourceField.getName());
                    field.setAccessible(true);
                    field.set(target, sourceField.get(source));
                }catch(Exception ex){}

            }
        }
        */
}

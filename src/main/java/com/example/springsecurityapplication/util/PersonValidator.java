package com.example.springsecurityapplication.util;

import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    //в данном методе указываем для какой модели предназначен данный валидатор
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    //В данном методе прописываем правила валидации
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person)target;
        if(personService.findByLogin(person) != null){
            errors.rejectValue("login", "", "Данный логин уже занят");
        }
    }
}

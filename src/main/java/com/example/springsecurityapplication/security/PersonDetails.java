package com.example.springsecurityapplication.security;

import com.example.springsecurityapplication.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class PersonDetails implements UserDetails {
    private final Person person;
    @Autowired
    public PersonDetails(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getLogin();
    }

    //Аккаунт действителен
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Аккаунт не заблокирован
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Пароль действителен
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Аккаунт активен
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Person getPerson(){
        return this.person;
    }
}

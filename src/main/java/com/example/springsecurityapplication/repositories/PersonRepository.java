package com.example.springsecurityapplication.repositories;

import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByLogin(String login);

    @Query(value = "UPDATE person SET role=?1 WHERE id = ?2;", nativeQuery = true)
    List<Person> updateUserRole(String role, int id);

}

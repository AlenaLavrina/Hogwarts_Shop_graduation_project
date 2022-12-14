package com.example.springsecurityapplication.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 5, max = 100, message = "Значение в поле должно содержать от 5 до 100 символов")
    @Column(name="login")
    private String login;

    @NotEmpty(message = "Поле не может быть пустым")
//    @Size(min = 5, max = 100, message = "Значение в поле должно содержать от 5 до 100 символов")
    @Column(name="password")
//    @Max(value = 100, message = "Пароль не может содержать более 100 символов")
    @Pattern(regexp="(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Пароль должен содержать не менее 8 символов, хотя бы одну цифру, спецсимвол, букву в верхнем регистре и в нижнем регистре")
    private String password;

    @Column(name = "role")
    private String role;

    @ManyToMany()
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> product;

    @OneToMany(mappedBy = "person")
    private List<Order> orderList;

    public Person() {
    }
    public Person(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

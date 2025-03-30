package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "users") //liga esta classe à tabela "users"
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id automático
    private Long id;

    private String name;
    private String email;

    public User() {}

    public User (String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters e Setters (obrigatórios para o JPA)
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
}

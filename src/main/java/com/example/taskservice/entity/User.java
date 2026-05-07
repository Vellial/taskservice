package com.example.taskservice.entity;

@Entity
@Table(name = "user")
public class User {
    private UUID id;
    private String name;
    private String email;
}

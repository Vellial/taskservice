package com.example.taskservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String name;
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Task> tasks;
}

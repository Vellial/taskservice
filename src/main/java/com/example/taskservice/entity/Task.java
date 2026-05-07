package com.example.taskservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "task")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private UUID id;
    private String title;
    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;
    private String description;
    private TaskStatus status;

}

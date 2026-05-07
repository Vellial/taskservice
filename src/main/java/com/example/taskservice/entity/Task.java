package com.example.taskservice.entity;

import java.util.UUID;

@Entity
@Table(name = "task")
public class Task {
    private UUID id;
    private String title;
    private User executor;
    private String description;
    private TaskStatus status;
}

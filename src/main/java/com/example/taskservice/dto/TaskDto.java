package com.example.taskservice.dto;

import com.example.taskservice.entity.TaskStatus;

import java.util.UUID;

public record TaskDto(
        UUID id,
        String title,
        String description,
        UUID executorId,
        TaskStatus status
) {
}

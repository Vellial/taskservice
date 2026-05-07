package com.example.taskservice.dto;

public record TaskDto(
        id, title, description, executorId, status
) {
}

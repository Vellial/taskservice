package com.example.taskservice.dto;

import com.example.taskservice.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TaskDto(
        @NotNull
        UUID id,
        @NotBlank
        String title,
        String description,
        UUID executorId,
        TaskStatus status
) {
}

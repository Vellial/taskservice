package com.example.taskservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskEventDto(
        String eventType,
        UUID taskId,
        UUID assigneeId,
        LocalDateTime timestamp
) {}

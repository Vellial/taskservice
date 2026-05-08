package com.example.taskservice.kafka;

import com.example.taskservice.dto.TaskEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TaskEventProducer {

    private final KafkaTemplate<String, TaskEventDto> kafkaTemplate;

    public TaskEventProducer(KafkaTemplate<String, TaskEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, TaskEventDto>> sendTaskEvent(TaskEventDto event) {
        return kafkaTemplate.send("task-events", event);
    }
}


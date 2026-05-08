package com.example.taskservice.kafka;

import com.example.taskservice.dto.TaskEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TaskEventProducer {

    private final KafkaTemplate<String, TaskEventDto> kafkaTemplate;

    public TaskEventProducer(KafkaTemplate<String, TaskEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTaskEvent(TaskEventDto event) {
        kafkaTemplate.send("task-events", event);
    }
}

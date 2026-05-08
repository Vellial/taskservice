package com.example.taskservice.kafka;

import com.example.taskservice.dto.TaskEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TaskEventTestListener {

    private final BlockingQueue<TaskEventDto> events = new LinkedBlockingQueue<>();

    @KafkaListener(topics = "task-events", groupId = "test-group")
    public void listen(TaskEventDto event) {
        log.info("Received: {}", event);
        events.add(event);
    }

    public TaskEventDto poll() throws InterruptedException {
        return events.poll(5, TimeUnit.SECONDS);
    }
}

package com.example.taskservice;

import com.example.taskservice.dto.TaskEventDto;
import com.example.taskservice.kafka.TaskEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(
        partitions = 1,
        topics = {"task-events"},
        bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
public class TaskEventProducerIntegrationTest {

    @Autowired
    private TaskEventProducer eventProducer;

    @Test
    void sendTaskEvent_ProducesMessageToTopic() throws InterruptedException, ExecutionException, TimeoutException {
        TaskEventDto event = new TaskEventDto(
                "TASK_CREATED",
                UUID.randomUUID(),
                UUID. randomUUID(),
                LocalDateTime.now()
        );
        var result = eventProducer.sendTaskEvent(event).get(5, TimeUnit.SECONDS);

        assertThat(result).isNotNull();
        assertThat(result.getRecordMetadata().topic()).isEqualTo("task-events");
    }
}

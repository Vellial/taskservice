package com.example.taskservice;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.entity.Task;
import com.example.taskservice.entity.TaskStatus;
import com.example.taskservice.entity.User;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.kafka.TaskEventProducer;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.TaskService;
import com.example.taskservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskEventProducer eventProducer;

    @InjectMocks
    private TaskService taskService;

    private UUID taskId;
    private UUID executorId;
    private User executor;
    private TaskDto taskDto;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskId = UUID.randomUUID();
        executorId = UUID.randomUUID();
        executor = User.builder().id(executorId).name("John").email("john@example.com").build();
        taskDto = new TaskDto(
                UUID.randomUUID(),
                "TITLE",
                "Description",
                executorId,
                TaskStatus.TODO
        );
        task = Task.builder()
                .id(taskId)
                .title(taskDto.title())
                .description(taskDto.description())
                .executor(executor)
                .status(taskDto.status())
                .build();
    }

    @Test
    void createTask_SendsTaskCreatedEvent() {
        when(userService.getUserById(executorId)).thenReturn(executor);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.createTask(taskDto);

        verify(eventProducer).sendTaskEvent(argThat(event ->
                "TASK_CREATED".equals(event.eventType()) &&
                        taskId.equals(event.taskId()) &&
                        executorId.equals(event.executorId()) &&
                        event.timestamp() != null
        ));
    }

    @Test
    void assignExecutor_SendsAssigneeChangedEvent() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userService.getUserById(executorId)).thenReturn(executor);

        taskService.assignExecutor(taskId, executorId);

        verify(eventProducer).sendTaskEvent(argThat(event ->
                "ASSIGNEE_CHANGED".equals(event.eventType()) &&
                        taskId.equals(event.taskId()) &&
                        executorId.equals(event.executorId()) &&
                        event.timestamp() != null
        ));
    }

    @Test
    void updateStatus_DoesNotSendEvent() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.updateStatus(taskId, TaskStatus.IN_PROGRESS);

        verify(eventProducer, never()).sendTaskEvent(any());
    }

    @Test
    void getTaskById_ThrowsException_WhenNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId));
    }

    @Test
    void getAllTasks_ReturnsPageOfDtos() {
        Page<Task> taskPage = new PageImpl<>(List.of(task), PageRequest.of(0, 10), 1);
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(taskPage);

        Page<TaskDto> result = taskService.getAllTasks(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals(taskId, result.getContent().get(0).id());
    }
}

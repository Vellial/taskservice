package com.example.taskservice.service;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.dto.TaskEventDto;
import com.example.taskservice.entity.Task;
import com.example.taskservice.entity.TaskStatus;
import com.example.taskservice.entity.User;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.kafka.TaskEventProducer;
import com.example.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserService userService;
    private final TaskRepository taskRepository;
    private final TaskEventProducer eventProducer;

    public Page<TaskDto> getAllTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);

        return tasks.map(this::toResponseDto);
    }

    public TaskDto getTaskById(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return toResponseDto(task);
    }

    public TaskDto createTask(TaskDto task) {
        User executor = userService.getUserById(task.executorId());
        Task saved = taskRepository.save(Task.builder()
                        .title(task.title())
                        .executor(executor)
                        .description(task.description())
                        .status(task.status())
                        .build());

        eventProducer.sendTaskEvent(new TaskEventDto(
                "TASK_CREATED",
                saved.getId(),
                saved.getExecutor().getId(),
                LocalDateTime.now()
        ));

        return toResponseDto(saved);
    }

    @Transactional
    public TaskDto assignExecutor(UUID id, UUID executorId) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        User executor = userService.getUserById(executorId);
        task.setExecutor(executor);

        eventProducer.sendTaskEvent(new TaskEventDto(
                "ASSIGNEE_CHANGED",
                task.getId(),
                executorId,
                LocalDateTime.now()
        ));

        return toResponseDto(task);
    }

    @Transactional
    public TaskDto updateStatus(UUID id, TaskStatus status) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(status);

        return toResponseDto(task);
    }

    private TaskDto toResponseDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getExecutor().getId(),
                task.getStatus());
    }
}

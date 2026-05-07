package com.example.taskservice.service;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.entity.Task;
import com.example.taskservice.entity.TaskStatus;
import com.example.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Page<TaskDto> getAllTasks(Pageable pageable) {
        return null;
    }

    public TaskDto getTaskById(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow();
        return toResponseDto(task);
    }

    public TaskDto createTask(Task task) {
        return null;
    }

    public TaskDto assignExecutor(UUID id, UUID executorId) {
        return null;
    }

    public TaskDto updateStatus(UUID id, TaskStatus status) {
        return null;
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

package com.example.taskservice.controller;

@RestController
@RequestMapping("/api/tasks")
@RequierdArgsConstructor
public class TaskController {
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<Task>> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getAllTasks(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PatchMapping("/{id}/executor")
    public ResponseEntity<Task> assignExecutor(@PathVariable UUID id, @RequestParam UUID executorId) {
        return ResponseEntity.ok(taskService.assignExecutor(id, executorId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable UUID id, @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }
}

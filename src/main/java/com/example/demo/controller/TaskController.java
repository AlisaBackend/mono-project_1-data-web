package com.example.demo.controller;

import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<TaskResponse> getAll(@AuthenticationPrincipal UserDetails principal) {
        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        return taskRepository.findAllByUserId(user.getId())
                .stream().map(this::toResponse).toList();
    }

    @PostMapping
    public TaskResponse create(@AuthenticationPrincipal UserDetails principal,
                               @RequestBody TaskRequest req) {
        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        Task task = Task.builder()
                .title(req.title())
                .description(req.description())
                .completed(req.completed() != null && req.completed())
                .user(user)
                .build();
        return toResponse(taskRepository.save(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@AuthenticationPrincipal UserDetails principal,
                                               @PathVariable Long id,
                                               @RequestBody TaskRequest req) {
        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        return taskRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .map(t -> {
                    t.setTitle(req.title());
                    t.setDescription(req.description());
                    if (req.completed() != null) t.setCompleted(req.completed());
                    return ResponseEntity.ok(toResponse(taskRepository.save(t)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails principal,
                                       @PathVariable Long id) {
        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        taskRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .ifPresentOrElse(taskRepository::delete, () -> { throw new RuntimeException("Not found"); });
        return ResponseEntity.noContent().build();
    }

    private TaskResponse toResponse(Task t) {
        return new TaskResponse(t.getId(), t.getTitle(), t.getDescription(),
                t.getCompleted(), t.getCreatedAt());
    }
}
// [file name]: AdminTaskController.java
package com.palmbeachresort.controller.task;

import com.palmbeachresort.dto.task.*;
import com.palmbeachresort.entity.task.Task;
import com.palmbeachresort.service.task.TaskService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tasks")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdminTaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Create a new task
     * POST /api/admin/tasks
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest,
                                                   HttpSession session) {
        Long adminId = (Long) session.getAttribute("userId");
        if (adminId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TaskResponse task = taskService.createTask(taskRequest, adminId);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    /**
     * Get all tasks
     * GET /api/admin/tasks
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get task by ID
     * GET /api/admin/tasks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    /**
     * Update task
     * PUT /api/admin/tasks/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
                                                   @Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse task = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(task);
    }

    /**
     * Assign task to staff
     * POST /api/admin/tasks/{id}/assign/{staffId}
     */
    @PostMapping("/{id}/assign/{staffId}")
    public ResponseEntity<TaskResponse> assignTask(@PathVariable Long id,
                                                   @PathVariable Long staffId) {
        TaskResponse task = taskService.assignTask(id, staffId);
        return ResponseEntity.ok(task);
    }

    /**
     * Update task status
     * PATCH /api/admin/tasks/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id,
                                                         @Valid @RequestBody TaskStatusUpdateRequest statusUpdate) {
        TaskResponse task = taskService.updateTaskStatus(id, statusUpdate);
        return ResponseEntity.ok(task);
    }

    /**
     * Delete task
     * DELETE /api/admin/tasks/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get tasks by status
     * GET /api/admin/tasks/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable Task.Status status) {
        List<TaskResponse> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get tasks by priority
     * GET /api/admin/tasks/priority/{priority}
     */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskResponse>> getTasksByPriority(@PathVariable Task.Priority priority) {
        List<TaskResponse> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get overdue tasks
     * GET /api/admin/tasks/overdue
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<TaskResponse>> getOverdueTasks() {
        List<TaskResponse> tasks = taskService.getOverdueTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get tasks due today
     * GET /api/admin/tasks/due-today
     */
    @GetMapping("/due-today")
    public ResponseEntity<List<TaskResponse>> getTasksDueToday() {
        List<TaskResponse> tasks = taskService.getTasksDueToday();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Add comment to task
     * POST /api/admin/tasks/{id}/comments
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<TaskCommentResponse> addComment(@PathVariable Long id,
                                                          @Valid @RequestBody TaskCommentRequest commentRequest,
                                                          HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("role");

        if (userId == null || userRole == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TaskCommentResponse comment = taskService.addComment(id, commentRequest, userId, userRole);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    /**
     * Get task comments
     * GET /api/admin/tasks/{id}/comments
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<TaskCommentResponse>> getTaskComments(@PathVariable Long id) {
        List<TaskCommentResponse> comments = taskService.getTaskComments(id);
        return ResponseEntity.ok(comments);
    }

    /**
     * Get task statistics
     * GET /api/admin/tasks/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<TaskStatistics> getTaskStatistics() {
        TaskStatistics statistics = taskService.getTaskStatistics();
        return ResponseEntity.ok(statistics);
    }
}
// [file name]: StaffTaskController.java
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
@RequestMapping("/api/staff/tasks")
@CrossOrigin(origins = {"https://frontend-palmbeachresort.vercel.app", "http://localhost:3000"},
        allowCredentials = "true")
public class StaffTaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Get staff's assigned tasks
     * GET /api/staff/tasks
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getStaffTasks(HttpSession session) {
        Long staffId = (Long) session.getAttribute("userId");
        if (staffId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<TaskResponse> tasks = taskService.getStaffTasks(staffId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get specific task
     * GET /api/staff/tasks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id, HttpSession session) {
        Long staffId = (Long) session.getAttribute("userId");
        if (staffId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TaskResponse task = taskService.getTaskById(id);

        // Verify staff is assigned to this task
        if (task.getAssignedTo() == null || !task.getAssignedTo().equals(staffId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(task);
    }

    /**
     * Update task status (TODO, IN_PROGRESS, DONE)
     * PATCH /api/staff/tasks/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id,
                                                         @Valid @RequestBody TaskStatusUpdateRequest statusUpdate,
                                                         HttpSession session) {
        Long staffId = (Long) session.getAttribute("userId");
        if (staffId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TaskResponse task = taskService.updateTaskStatus(id, staffId, statusUpdate);
        return ResponseEntity.ok(task);
    }

    /**
     * Add comment to task
     * POST /api/staff/tasks/{id}/comments
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
     * GET /api/staff/tasks/{id}/comments
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<TaskCommentResponse>> getTaskComments(@PathVariable Long id) {
        List<TaskCommentResponse> comments = taskService.getTaskComments(id);
        return ResponseEntity.ok(comments);
    }

    /**
     * Get staff task statistics
     * GET /api/staff/tasks/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<TaskStatistics> getStaffTaskStatistics(HttpSession session) {
        Long staffId = (Long) session.getAttribute("userId");
        if (staffId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TaskStatistics statistics = taskService.getStaffTaskStatistics(staffId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get tasks by status for staff
     * GET /api/staff/tasks/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable Task.Status status,
                                                               HttpSession session) {
        Long staffId = (Long) session.getAttribute("userId");
        if (staffId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<TaskResponse> allTasks = taskService.getStaffTasks(staffId);
        List<TaskResponse> filteredTasks = allTasks.stream()
                .filter(task -> task.getStatus() == status)
                .toList();

        return ResponseEntity.ok(filteredTasks);
    }
}
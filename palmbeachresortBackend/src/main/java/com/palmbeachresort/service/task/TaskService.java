package com.palmbeachresort.service.task;

import com.palmbeachresort.dto.task.*;
import com.palmbeachresort.entity.task.Task;
import java.util.List;

public interface TaskService {

    // Admin operations
    TaskResponse createTask(TaskRequest taskRequest, Long adminId);
    TaskResponse updateTask(Long taskId, TaskRequest taskRequest);
    TaskResponse assignTask(Long taskId, Long staffId);
    TaskResponse updateTaskStatus(Long taskId, TaskStatusUpdateRequest statusUpdate);
    void deleteTask(Long taskId);
    List<TaskResponse> getAllTasks();
    List<TaskResponse> getTasksByStatus(Task.Status status);
    List<TaskResponse> getTasksByPriority(Task.Priority priority);

    // Staff operation
    List<TaskResponse> getStaffTasks(Long staffId);
    TaskResponse getTaskById(Long taskId);
    TaskResponse updateTaskStatus(Long taskId, Long staffId, TaskStatusUpdateRequest statusUpdate);

    // Comment operations
    TaskCommentResponse addComment(Long taskId, TaskCommentRequest commentRequest, Long userId, String userRole);
    List<TaskCommentResponse> getTaskComments(Long taskId);

    // Utility methods
    List<TaskResponse> getOverdueTasks();
    List<TaskResponse> getTasksDueToday();

    // Statistics
    TaskStatistics getTaskStatistics();
    TaskStatistics getStaffTaskStatistics(Long staffId);
}
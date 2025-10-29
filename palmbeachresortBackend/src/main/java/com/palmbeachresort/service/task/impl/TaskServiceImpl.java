// [file name]: TaskServiceImpl.java
package com.palmbeachresort.service.task.impl;

import com.palmbeachresort.dto.task.*;
import com.palmbeachresort.entity.task.Task;
import com.palmbeachresort.entity.task.TaskComment;
import com.palmbeachresort.entity.auth.Admin;
import com.palmbeachresort.entity.auth.Staff;
import com.palmbeachresort.repository.task.TaskRepository;
import com.palmbeachresort.repository.task.TaskCommentRepository;
import com.palmbeachresort.repository.auth.AdminRepository;
import com.palmbeachresort.repository.auth.StaffRepository;
import com.palmbeachresort.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskCommentRepository taskCommentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public TaskResponse createTask(TaskRequest taskRequest, Long adminId) {
        try {
            System.out.println("📝 Creating new task by admin: " + adminId);

            // Validate admin exists
            Admin admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new IllegalArgumentException("Admin not found with id: " + adminId));

            // Create task
            Task task = new Task();
            task.setTitle(taskRequest.getTitle());
            task.setDescription(taskRequest.getDescription());
            task.setPriority(taskRequest.getPriority());
            task.setDueDate(taskRequest.getDueDate());
            task.setCreatedBy(admin);

            // Assign to staff if provided
            if (taskRequest.getAssignedTo() != null) {
                Staff staff = staffRepository.findById(taskRequest.getAssignedTo())
                        .orElseThrow(() -> new IllegalArgumentException("Staff not found with id: " + taskRequest.getAssignedTo()));
                task.setAssignedTo(staff);
            }

            Task savedTask = taskRepository.save(task);
            System.out.println("✅ Task created with ID: " + savedTask.getId());

            // Send real-time notification if assigned
            if (savedTask.getAssignedTo() != null) {
                sendTaskAssignmentNotification(savedTask);
            }

            return convertToResponse(savedTask);

        } catch (Exception e) {
            System.err.println("❌ Error creating task: " + e.getMessage());
            throw new RuntimeException("Failed to create task: " + e.getMessage());
        }
    }

    @Override
    public TaskResponse updateTask(Long taskId, TaskRequest taskRequest) {
        try {
            System.out.println("✏️ Updating task: " + taskId);

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

            task.setTitle(taskRequest.getTitle());
            task.setDescription(taskRequest.getDescription());
            task.setPriority(taskRequest.getPriority());
            task.setDueDate(taskRequest.getDueDate());

            // Handle assignment
            if (taskRequest.getAssignedTo() != null) {
                Staff staff = staffRepository.findById(taskRequest.getAssignedTo())
                        .orElseThrow(() -> new IllegalArgumentException("Staff not found with id: " + taskRequest.getAssignedTo()));
                task.setAssignedTo(staff);

                // Send notification for new assignment
                sendTaskAssignmentNotification(task);
            } else {
                task.setAssignedTo(null);
            }

            Task updatedTask = taskRepository.save(task);
            return convertToResponse(updatedTask);

        } catch (Exception e) {
            System.err.println("❌ Error updating task: " + e.getMessage());
            throw new RuntimeException("Failed to update task: " + e.getMessage());
        }
    }

    @Override
    public TaskResponse assignTask(Long taskId, Long staffId) {
        try {
            System.out.println("👤 Assigning task " + taskId + " to staff: " + staffId);

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

            Staff staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with id: " + staffId));

            task.setAssignedTo(staff);
            Task updatedTask = taskRepository.save(task);

            // Send real-time notification
            sendTaskAssignmentNotification(updatedTask);

            return convertToResponse(updatedTask);

        } catch (Exception e) {
            System.err.println("❌ Error assigning task: " + e.getMessage());
            throw new RuntimeException("Failed to assign task: " + e.getMessage());
        }
    }

    @Override
    public TaskResponse updateTaskStatus(Long taskId, TaskStatusUpdateRequest statusUpdate) {
        try {
            System.out.println("🔄 Updating task status: " + taskId + " to " + statusUpdate.getStatus());

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

            task.setStatus(statusUpdate.getStatus());
            Task updatedTask = taskRepository.save(task);

            // Send real-time notification
            sendTaskStatusNotification(updatedTask);

            return convertToResponse(updatedTask);

        } catch (Exception e) {
            System.err.println("❌ Error updating task status: " + e.getMessage());
            throw new RuntimeException("Failed to update task status: " + e.getMessage());
        }
    }

    @Override
    public TaskResponse updateTaskStatus(Long taskId, Long staffId, TaskStatusUpdateRequest statusUpdate) {
        try {
            System.out.println("🔄 Staff updating task status: " + taskId + " by staff: " + staffId);

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

            // Verify staff is assigned to this task
            if (task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(staffId)) {
                throw new IllegalArgumentException("Staff is not assigned to this task");
            }

            task.setStatus(statusUpdate.getStatus());
            Task updatedTask = taskRepository.save(task);

            // Send real-time notification
            sendTaskStatusNotification(updatedTask);

            return convertToResponse(updatedTask);

        } catch (Exception e) {
            System.err.println("❌ Error updating task status: " + e.getMessage());
            throw new RuntimeException("Failed to update task status: " + e.getMessage());
        }
    }

    @Override
    public void deleteTask(Long taskId) {
        try {
            System.out.println("🗑️ Deleting task: " + taskId);

            if (!taskRepository.existsById(taskId)) {
                throw new IllegalArgumentException("Task not found with id: " + taskId);
            }

            taskRepository.deleteById(taskId);
            System.out.println("✅ Task deleted successfully");

        } catch (Exception e) {
            System.err.println("❌ Error deleting task: " + e.getMessage());
            throw new RuntimeException("Failed to delete task: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        try {
            List<Task> tasks = taskRepository.findAllWithDetails();
            return tasks.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting all tasks: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve tasks: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByStatus(Task.Status status) {
        try {
            List<Task> tasks = taskRepository.findByStatusOrderByCreatedAtDesc(status);
            return tasks.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting tasks by status: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve tasks: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByPriority(Task.Priority priority) {
        try {
            List<Task> tasks = taskRepository.findByPriorityOrderByCreatedAtDesc(priority);
            return tasks.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting tasks by priority: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve tasks: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getStaffTasks(Long staffId) {
        try {
            List<Task> tasks = taskRepository.findByAssignedToIdOrderByCreatedAtDesc(staffId);
            return tasks.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting staff tasks: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve staff tasks: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long taskId) {
        try {
            Task task = taskRepository.findByIdWithDetails(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
            return convertToResponse(task);
        } catch (Exception e) {
            System.err.println("❌ Error getting task by ID: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve task: " + e.getMessage());
        }
    }

    @Override
    public TaskCommentResponse addComment(Long taskId, TaskCommentRequest commentRequest, Long userId, String userRole) {
        try {
            System.out.println("💬 Adding comment to task: " + taskId + " by user: " + userId);

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

            TaskComment comment = new TaskComment(task, userId, userRole, commentRequest.getComment());
            TaskComment savedComment = taskCommentRepository.save(comment);

            // Send real-time notification
            sendTaskCommentNotification(task, savedComment);

            return convertCommentToResponse(savedComment);

        } catch (Exception e) {
            System.err.println("❌ Error adding comment: " + e.getMessage());
            throw new RuntimeException("Failed to add comment: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskCommentResponse> getTaskComments(Long taskId) {
        try {
            List<TaskComment> comments = taskCommentRepository.findByTaskIdWithDetails(taskId);
            return comments.stream()
                    .map(this::convertCommentToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting task comments: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve comments: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getOverdueTasks() {
        try {
            List<Task> tasks = taskRepository.findOverdueTasks(LocalDate.now());
            return tasks.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting overdue tasks: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve overdue tasks: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksDueToday() {
        try {
            List<Task> tasks = taskRepository.findTasksDueToday(LocalDate.now());
            return tasks.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting tasks due today: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve tasks due today: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TaskStatistics getTaskStatistics() {
        try {
            Long totalTasks = taskRepository.count();
            Long todoCount = taskRepository.countByAssignedToAndStatus(null, Task.Status.TODO);
            Long inProgressCount = taskRepository.countByAssignedToAndStatus(null, Task.Status.IN_PROGRESS);
            Long doneCount = taskRepository.countByAssignedToAndStatus(null, Task.Status.DONE);
            Long cancelledCount = taskRepository.countByAssignedToAndStatus(null, Task.Status.CANCELLED);

            // Convert int to Long using Long.valueOf()
            Long overdueCount = Long.valueOf(taskRepository.findOverdueTasks(LocalDate.now()).size());
            Long urgentCount = Long.valueOf(taskRepository.findByPriorityOrderByCreatedAtDesc(Task.Priority.URGENT).size());

            return new TaskStatistics(totalTasks, todoCount, inProgressCount, doneCount,
                    cancelledCount, overdueCount, urgentCount);
        } catch (Exception e) {
            System.err.println("❌ Error getting task statistics: " + e.getMessage());
            return new TaskStatistics(0L, 0L, 0L, 0L, 0L, 0L, 0L);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TaskStatistics getStaffTaskStatistics(Long staffId) {
        try {
            Long totalTasks = Long.valueOf(taskRepository.findByAssignedToIdOrderByCreatedAtDesc(staffId).size());
            Long todoCount = taskRepository.countByAssignedToAndStatus(staffId, Task.Status.TODO);
            Long inProgressCount = taskRepository.countByAssignedToAndStatus(staffId, Task.Status.IN_PROGRESS);
            Long doneCount = taskRepository.countByAssignedToAndStatus(staffId, Task.Status.DONE);
            Long cancelledCount = taskRepository.countByAssignedToAndStatus(staffId, Task.Status.CANCELLED);

            // For stream count, we need to collect to list first or use casting
            List<Task> overdueTasks = taskRepository.findOverdueTasks(LocalDate.now());
            Long overdueCount = Long.valueOf(overdueTasks.stream()
                    .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().getId().equals(staffId))
                    .count());

            List<Task> urgentTasks = taskRepository.findByPriorityOrderByCreatedAtDesc(Task.Priority.URGENT);
            Long urgentCount = Long.valueOf(urgentTasks.stream()
                    .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().getId().equals(staffId))
                    .count());

            return new TaskStatistics(totalTasks, todoCount, inProgressCount, doneCount,
                    cancelledCount, overdueCount, urgentCount);
        } catch (Exception e) {
            System.err.println("❌ Error getting staff task statistics: " + e.getMessage());
            return new TaskStatistics(0L, 0L, 0L, 0L, 0L, 0L, 0L);
        }
    }

    // Utility methods
    private TaskResponse convertToResponse(Task task) {
        boolean overdue = task.getDueDate() != null &&
                task.getDueDate().isBefore(LocalDate.now()) &&
                task.getStatus() != Task.Status.DONE &&
                task.getStatus() != Task.Status.CANCELLED;

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate(),
                task.getAssignedTo() != null ? task.getAssignedTo().getId() : null,
                task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : "Unassigned",
                task.getCreatedBy().getId(),
                task.getCreatedBy().getFullName(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getCompletedAt(),
                overdue
        );
    }

    private TaskCommentResponse convertCommentToResponse(TaskComment comment) {
        // In a real application, you would fetch user details from user service
        String userName = "User"; // This should come from user service

        return new TaskCommentResponse(
                comment.getId(),
                comment.getTask().getId(),
                comment.getUserId(),
                comment.getUserRole(),
                userName,
                comment.getComment(),
                comment.getCreatedAt()
        );
    }

    private void sendTaskAssignmentNotification(Task task) {
        try {
            if (task.getAssignedTo() != null) {
                TaskResponse response = convertToResponse(task);
                messagingTemplate.convertAndSendToUser(
                        task.getAssignedTo().getId().toString(),
                        "/queue/task-assignments",
                        response
                );
                System.out.println("📢 Task assignment notification sent to staff: " + task.getAssignedTo().getId());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Warning: Could not send task assignment notification: " + e.getMessage());
        }
    }

    private void sendTaskStatusNotification(Task task) {
        try {
            TaskResponse response = convertToResponse(task);

            // Notify admin who created the task
            messagingTemplate.convertAndSendToUser(
                    task.getCreatedBy().getId().toString(),
                    "/queue/task-updates",
                    response
            );

            // Notify all admins for major updates
            messagingTemplate.convertAndSend("/topic/admin/task-updates", response);

            System.out.println("📢 Task status notification sent");
        } catch (Exception e) {
            System.err.println("⚠️ Warning: Could not send task status notification: " + e.getMessage());
        }
    }

    private void sendTaskCommentNotification(Task task, TaskComment comment) {
        try {
            TaskCommentResponse response = convertCommentToResponse(comment);

            // Notify task creator and assigned staff
            messagingTemplate.convertAndSendToUser(
                    task.getCreatedBy().getId().toString(),
                    "/queue/task-comments",
                    response
            );

            if (task.getAssignedTo() != null) {
                messagingTemplate.convertAndSendToUser(
                        task.getAssignedTo().getId().toString(),
                        "/queue/task-comments",
                        response
                );
            }

            System.out.println("📢 Task comment notification sent");
        } catch (Exception e) {
            System.err.println("⚠️ Warning: Could not send task comment notification: " + e.getMessage());
        }
    }
}
// [file name]: TaskRequest.java
package com.palmbeachresort.dto.task;

import com.palmbeachresort.entity.task.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Task.Priority priority = Task.Priority.MEDIUM;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    private Long assignedTo; // Staff ID

    // Constructors
    public TaskRequest() {}

    public TaskRequest(String title, String description, Task.Priority priority,
                       LocalDate dueDate, Long assignedTo) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.assignedTo = assignedTo;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Task.Priority getPriority() { return priority; }
    public void setPriority(Task.Priority priority) { this.priority = priority; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Long getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Long assignedTo) { this.assignedTo = assignedTo; }
}
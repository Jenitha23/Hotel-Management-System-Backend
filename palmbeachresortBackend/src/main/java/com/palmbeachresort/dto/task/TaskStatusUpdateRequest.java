// [file name]: TaskStatusUpdateRequest.java
package com.palmbeachresort.dto.task;

import com.palmbeachresort.entity.task.Task;
import jakarta.validation.constraints.NotNull;

public class TaskStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private Task.Status status;

    // Constructors
    public TaskStatusUpdateRequest() {}

    public TaskStatusUpdateRequest(Task.Status status) {
        this.status = status;
    }

    // Getters and Setters
    public Task.Status getStatus() { return status; }
    public void setStatus(Task.Status status) { this.status = status; }
}
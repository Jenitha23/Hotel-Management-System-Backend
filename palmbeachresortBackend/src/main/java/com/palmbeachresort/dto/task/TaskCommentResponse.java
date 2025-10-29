// [file name]: TaskCommentResponse.java
package com.palmbeachresort.dto.task;

import java.time.LocalDateTime;

public class TaskCommentResponse {

    private Long id;
    private Long taskId;
    private Long userId;
    private String userRole;
    private String userName;
    private String comment;
    private LocalDateTime createdAt;

    // Constructors
    public TaskCommentResponse() {}

    public TaskCommentResponse(Long id, Long taskId, Long userId, String userRole,
                               String userName, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.userRole = userRole;
        this.userName = userName;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
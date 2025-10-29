// [file name]: TaskCommentRequest.java
package com.palmbeachresort.dto.task;

import jakarta.validation.constraints.NotBlank;

public class TaskCommentRequest {

    @NotBlank(message = "Comment is required")
    private String comment;

    // Constructors
    public TaskCommentRequest() {}

    public TaskCommentRequest(String comment) {
        this.comment = comment;
    }

    // Getters and Setters
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
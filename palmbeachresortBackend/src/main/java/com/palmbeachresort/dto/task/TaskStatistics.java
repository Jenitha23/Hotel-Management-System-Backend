// [file name]: TaskStatistics.java
package com.palmbeachresort.dto.task;

public class TaskStatistics {
    private Long totalTasks;
    private Long todoCount;
    private Long inProgressCount;
    private Long doneCount;
    private Long cancelledCount;
    private Long overdueCount;
    private Long urgentCount;

    // Constructors, Getters and Setters
    public TaskStatistics() {}

    public TaskStatistics(Long totalTasks, Long todoCount, Long inProgressCount,
                          Long doneCount, Long cancelledCount, Long overdueCount, Long urgentCount) {
        this.totalTasks = totalTasks;
        this.todoCount = todoCount;
        this.inProgressCount = inProgressCount;
        this.doneCount = doneCount;
        this.cancelledCount = cancelledCount;
        this.overdueCount = overdueCount;
        this.urgentCount = urgentCount;
    }

    // Getters and Setters
    public Long getTotalTasks() { return totalTasks; }
    public void setTotalTasks(Long totalTasks) { this.totalTasks = totalTasks; }

    public Long getTodoCount() { return todoCount; }
    public void setTodoCount(Long todoCount) { this.todoCount = todoCount; }

    public Long getInProgressCount() { return inProgressCount; }
    public void setInProgressCount(Long inProgressCount) { this.inProgressCount = inProgressCount; }

    public Long getDoneCount() { return doneCount; }
    public void setDoneCount(Long doneCount) { this.doneCount = doneCount; }

    public Long getCancelledCount() { return cancelledCount; }
    public void setCancelledCount(Long cancelledCount) { this.cancelledCount = cancelledCount; }

    public Long getOverdueCount() { return overdueCount; }
    public void setOverdueCount(Long overdueCount) { this.overdueCount = overdueCount; }

    public Long getUrgentCount() { return urgentCount; }
    public void setUrgentCount(Long urgentCount) { this.urgentCount = urgentCount; }
}
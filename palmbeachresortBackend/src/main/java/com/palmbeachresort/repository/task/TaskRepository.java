package com.palmbeachresort.repository.task;

import com.palmbeachresort.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find tasks assigned to a staff member
    List<Task> findByAssignedToIdOrderByCreatedAtDesc(Long staffId);

    // Find tasks by status
    List<Task> findByStatusOrderByCreatedAtDesc(Task.Status status);

    // Find tasks by priority
    List<Task> findByPriorityOrderByCreatedAtDesc(Task.Priority priority);

    // Find overdue tasks
    @Query("SELECT t FROM Task t WHERE t.dueDate < :today AND t.status != 'DONE' AND t.status != 'CANCELLED'")
    List<Task> findOverdueTasks(@Param("today") LocalDate today);

    // Find tasks due today
    @Query("SELECT t FROM Task t WHERE t.dueDate = :today AND t.status != 'DONE' AND t.status != 'CANCELLED'")
    List<Task> findTasksDueToday(@Param("today") LocalDate today);

    // Find tasks with staff details - FIXED METHOD
    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.assignedTo LEFT JOIN FETCH t.createdBy WHERE t.id = :id")
    Optional<Task> findByIdWithDetails(@Param("id") Long id);

    // Count tasks by status for a staff member - FIXED METHOD
    @Query("SELECT COUNT(t) FROM Task t WHERE (:staffId IS NULL OR t.assignedTo.id = :staffId) AND t.status = :status")
    Long countByAssignedToAndStatus(@Param("staffId") Long staffId, @Param("status") Task.Status status);

    // Find all tasks with details - FIXED METHOD
    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.assignedTo LEFT JOIN FETCH t.createdBy ORDER BY t.createdAt DESC")
    List<Task> findAllWithDetails();

    // Check if task exists - ADD THIS METHOD
    boolean existsById(Long id);
}
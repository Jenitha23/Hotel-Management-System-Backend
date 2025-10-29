// [file name]: TaskCommentRepository.java
package com.palmbeachresort.repository.task;

import com.palmbeachresort.entity.task.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {

    // Find comments for a task
    List<TaskComment> findByTaskIdOrderByCreatedAtDesc(Long taskId);

    // Find comments with user details
    @Query("SELECT tc FROM TaskComment tc WHERE tc.task.id = :taskId ORDER BY tc.createdAt DESC")
    List<TaskComment> findByTaskIdWithDetails(@Param("taskId") Long taskId);
}
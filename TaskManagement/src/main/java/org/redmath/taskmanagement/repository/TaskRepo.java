package org.redmath.taskmanagement.repository;

import org.redmath.taskmanagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task,Long> {
    List<Task> findByOwnerId(Long id);
}

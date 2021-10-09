package org.example.repository;

import org.example.entity.Project;
import org.example.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByProjectIdAndProjectUserLogin(int id, String login);

    Optional<Task> findByIdAndProjectIdAndProjectUserLogin(int id, int projectId, String login);

    boolean existsByIdAndProjectId(int id, int projectId);

    void deleteByIdAndProjectIdAndProjectUserLogin(int id, int projectId, String login);
}

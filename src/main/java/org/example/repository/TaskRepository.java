package org.example.repository;

import org.example.entity.Project;
import org.example.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByProjectIdAndProjectUserLoginOrderByPriority(int id, String login);

    Optional<Task> findByIdAndProjectIdAndProjectUserLogin(int id, int projectId, String login);

    boolean existsByIdAndProjectId(int id, int projectId);

    void deleteByIdAndProjectIdAndProjectUserLogin(int id, int projectId, String login);

    @Modifying
    @Query("UPDATE Task v SET v.priority=:priority WHERE v.id=:id")
    void updatePriority(@Param("priority") int priority, @Param("id") int id);
}

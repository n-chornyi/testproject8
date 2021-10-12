package org.example.repository;

import org.example.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findAllByUserLoginOrderById(String login);

    @EntityGraph(attributePaths = {"tasks"})
    Optional<Project> findByIdAndUserLogin(int id, String login);

    boolean existsByIdAndUserLogin(int id, String login);

    void deleteByIdAndUserLogin(int id, String login);
}

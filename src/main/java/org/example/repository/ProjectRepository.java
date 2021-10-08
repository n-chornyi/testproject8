package org.example.repository;

import org.example.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @EntityGraph(attributePaths = {"tasks"})
    List<Project> findAll();

    @EntityGraph(attributePaths = {"tasks"})
    Optional<Project> findById(int id);

    boolean existsById(int id);
}

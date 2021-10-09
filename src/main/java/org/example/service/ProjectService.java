package org.example.service;

import org.example.dto.project.BaseProjectDTO;
import org.example.dto.project.CreateProjectDTO;
import org.example.dto.project.UpdateProjectDTO;
import org.example.entity.Project;

import java.util.List;

public interface ProjectService {

    BaseProjectDTO save(CreateProjectDTO project, String login);

    BaseProjectDTO getById(int id, String login);

    void deleteById(int id, String login);

    List<BaseProjectDTO> getAll(String login);

    BaseProjectDTO updateById(int id, UpdateProjectDTO updateProjectDTO, String login);
}

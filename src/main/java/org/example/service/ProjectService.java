package org.example.service;

import org.example.dto.project.BaseProjectDTO;
import org.example.dto.project.CreateProjectDTO;
import org.example.dto.project.UpdateProjectDTO;
import org.example.entity.Project;

import java.util.List;

public interface ProjectService {

    BaseProjectDTO save(CreateProjectDTO project);

    BaseProjectDTO getById(int id);

    void deleteById(int id);

    List<BaseProjectDTO> getAll();

    BaseProjectDTO updateById(int id, UpdateProjectDTO updateProjectDTO);
}

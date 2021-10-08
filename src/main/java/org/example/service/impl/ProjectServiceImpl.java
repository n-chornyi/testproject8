package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.project.BaseProjectDTO;
import org.example.dto.project.CreateProjectDTO;
import org.example.dto.project.UpdateProjectDTO;
import org.example.entity.Project;
import org.example.entity.User;
import org.example.mapper.ProjectMapper;
import org.example.repository.ProjectRepository;
import org.example.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public BaseProjectDTO save(CreateProjectDTO createProjectDTO) {
        Project project = projectMapper.parseDTO(createProjectDTO);
        
        //TODO CHANGE USER
        User user = new User();
        user.setId(1);
        project.setUser(user);
        return projectMapper.getDTO(projectRepository.save(project));
    }

    @Override
    public BaseProjectDTO getById(int id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Error project 23")
        );

        return projectMapper.getDTO(project);
    }

    @Override
    public void deleteById(int id) {
        projectRepository.deleteById(id);
    }

    @Override
    public List<BaseProjectDTO> getAll() {
        return projectMapper.getAll(projectRepository.findAll());
    }

    @Override
    @Transactional
    public BaseProjectDTO updateById(int id, UpdateProjectDTO updateProjectDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(""));
        project = projectMapper.update(updateProjectDTO, project);
        return projectMapper.getDTO(projectRepository.save(project));
    }
}

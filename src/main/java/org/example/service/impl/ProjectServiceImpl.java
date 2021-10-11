package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.project.BaseProjectDTO;
import org.example.dto.project.CreateProjectDTO;
import org.example.dto.project.UpdateProjectDTO;
import org.example.entity.Project;
import org.example.entity.User;
import org.example.exception.NotFoundException;
import org.example.mapper.ProjectMapper;
import org.example.repository.ProjectRepository;
import org.example.repository.UserRepository;
import org.example.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BaseProjectDTO save(CreateProjectDTO createProjectDTO, String login) {
        Project project = projectMapper.parseDTO(createProjectDTO);
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("User not found"));
        project.setUser(user);
        return projectMapper.getDTO(projectRepository.save(project));
    }

    @Override
    public BaseProjectDTO getById(int id, String login) {
        Project project = projectRepository.findByIdAndUserLogin(id, login).orElseThrow(
                () -> new RuntimeException("Not found project")
        );

        return projectMapper.getDTO(project);
    }

    @Override
    @Transactional
    public void deleteById(int id, String login) {
        if (!projectRepository.existsByIdAndUserLogin(id,login)) {
            throw new NotFoundException("Not found project");
        }
        projectRepository.deleteByIdAndUserLogin(id, login);
    }

    @Override
    public List<BaseProjectDTO> getAll(String login) {
        return projectMapper.getAll(projectRepository.findAllByUserLoginOrderById(login));
    }

    @Override
    @Transactional
    public BaseProjectDTO updateById(int id, UpdateProjectDTO updateProjectDTO, String login) {
        Project project = projectRepository.findByIdAndUserLogin(id, login)
                .orElseThrow(() -> new RuntimeException(""));
        project = projectMapper.update(updateProjectDTO, project);
        return projectMapper.getDTO(projectRepository.save(project));
    }
}

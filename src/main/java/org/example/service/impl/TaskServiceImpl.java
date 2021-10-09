package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.task.BaseTaskDTO;
import org.example.dto.task.CreateTaskDTO;
import org.example.dto.task.UpdateTaskDTO;
import org.example.entity.Project;
import org.example.entity.Task;
import org.example.exception.NotFoundExeption;
import org.example.mapper.TasksMapper;
import org.example.repository.ProjectRepository;
import org.example.repository.TaskRepository;
import org.example.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TasksMapper tasksMapper;

    @Override
    public BaseTaskDTO save(int projectId, CreateTaskDTO createTaskDTO, String login) {
        if (!projectRepository.existsByIdAndUserLogin(projectId, login)) {
            throw new NotFoundExeption("Project not found");
        }
        Task task = tasksMapper.parseDTO(createTaskDTO);
        task.setProject(projectRepository.findById(projectId)
                .orElseThrow( () -> new NotFoundExeption("Task not found")));
        return tasksMapper.getDTO(taskRepository.save(task));
    }

    @Override
    public BaseTaskDTO getById(int projectId, int taskId, String login) {
        return tasksMapper.getDTO(taskRepository.findByIdAndProjectIdAndProjectUserLogin(taskId, projectId, login).orElseThrow(
                () -> new NotFoundExeption("Task not found")));
    }

    @Override
    @Transactional
    public void deleteById(int projectId, int taskId, String login) {
        if (!taskRepository.existsByIdAndProjectId(taskId, projectId)) {
            throw new NotFoundExeption("Task not found");
        }
        taskRepository.deleteByIdAndProjectIdAndProjectUserLogin(taskId, projectId, login);
    }

    @Override
    public List<BaseTaskDTO> getAll(int projectId, String login) {
        return tasksMapper.getAll(taskRepository.findAllByProjectIdAndProjectUserLogin(projectId, login));
    }

    @Override
    @Transactional
    public BaseTaskDTO updateById(int projectId, int taskId, UpdateTaskDTO updateTaskDTO, String login) {
        Task task = taskRepository.findByIdAndProjectIdAndProjectUserLogin(taskId, projectId, login).orElseThrow(
                () -> new NotFoundExeption("Task not found"));
        task = tasksMapper.update(updateTaskDTO, task);
        return tasksMapper.getDTO(taskRepository.save(task));
    }
}

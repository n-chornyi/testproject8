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
    public BaseTaskDTO save(int projectId, CreateTaskDTO createTaskDTO) {
        if (!projectRepository.existsById(projectId)) {
            throw new NotFoundExeption("Project not found");
        }
        Task task = tasksMapper.parseDTO(createTaskDTO);
        Project project = new Project();
        project.setId(projectId);
        task.setProject(project);
        return tasksMapper.getDTO(taskRepository.save(task));
    }

    @Override
    public BaseTaskDTO getById(int projectId, int taskId) {
        return tasksMapper.getDTO(taskRepository.findByIdAndProjectId(taskId, projectId).orElseThrow(
                () -> new NotFoundExeption("Task not found")));
    }

    @Override
    public void deleteById(int projectId, int taskId) {
        if (!taskRepository.existsByIdAndProjectId(taskId, projectId)) {
            throw new NotFoundExeption("Task not found");
        }
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<BaseTaskDTO> getAll(int projectId) {
        return tasksMapper.getAll(taskRepository.findAllByProjectId(projectId));
    }

    @Override
    @Transactional
    public BaseTaskDTO updateById(int projectId, int taskId, UpdateTaskDTO updateTaskDTO) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId).orElseThrow(
                () -> new NotFoundExeption("Task not found"));
        task = tasksMapper.update(updateTaskDTO, task);
        return tasksMapper.getDTO(taskRepository.save(task));
    }
}

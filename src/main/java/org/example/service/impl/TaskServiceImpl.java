package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.task.BaseTaskDTO;
import org.example.dto.task.CreateTaskDTO;
import org.example.dto.task.UpdatePriority;
import org.example.dto.task.UpdateTaskDTO;
import org.example.entity.Task;
import org.example.exception.NotFoundException;
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
            throw new NotFoundException("Project not found");
        }
        Task task = tasksMapper.parseDTO(createTaskDTO);
        task.setProject(projectRepository.findById(projectId)
                .orElseThrow( () -> new NotFoundException("Task not found")));
        return tasksMapper.getDTO(taskRepository.save(task));
    }

    @Override
    public BaseTaskDTO getById(int projectId, int taskId, String login) {
        return tasksMapper.getDTO(taskRepository.findByIdAndProjectIdAndProjectUserLogin(taskId, projectId, login).orElseThrow(
                () -> new NotFoundException("Task not found")));
    }

    @Override
    @Transactional
    public void deleteById(int projectId, int taskId, String login) {
        if (!taskRepository.existsByIdAndProjectId(taskId, projectId)) {
            throw new NotFoundException("Task not found");
        }
        taskRepository.deleteByIdAndProjectIdAndProjectUserLogin(taskId, projectId, login);
    }

    @Override
    public List<BaseTaskDTO> getAll(int projectId, String login) {
        return tasksMapper.getAll(taskRepository.findAllByProjectIdAndProjectUserLoginOrderByPriority(projectId, login));
    }

    @Override
    @Transactional
    public boolean updatePriority(int projectId, UpdatePriority update, String login) {
        if (!projectRepository.existsByIdAndUserLogin(projectId, login)) {
            throw new NotFoundException("Project not found");
        }
        String[] priority = update.getIndexes().split(";");
        for (int i = 0; i < priority.length; i++) {
            taskRepository.updatePriority(i, Integer.parseInt(priority[i]));
        }
        return true;
    }

    @Override
    @Transactional
    public BaseTaskDTO updateById(int projectId, int taskId, UpdateTaskDTO updateTaskDTO, String login) {
        Task task = taskRepository.findByIdAndProjectIdAndProjectUserLogin(taskId, projectId, login).orElseThrow(
                () -> new NotFoundException("Task not found"));
        task = tasksMapper.update(updateTaskDTO, task);
        return tasksMapper.getDTO(taskRepository.save(task));
    }
}

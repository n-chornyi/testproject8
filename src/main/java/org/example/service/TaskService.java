package org.example.service;

import org.example.dto.task.BaseTaskDTO;
import org.example.dto.task.CreateTaskDTO;
import org.example.dto.task.UpdatePriority;
import org.example.dto.task.UpdateTaskDTO;

import java.util.List;

public interface TaskService {

    BaseTaskDTO save(int projectId, CreateTaskDTO createTaskDTO, String login);

    BaseTaskDTO getById(int projectId, int TaskId, String login);

    void deleteById(int projectId, int taskId, String login);

    List<BaseTaskDTO> getAll(int projectId, String login);

    boolean updatePriority(int projectId, UpdatePriority update, String login);

    BaseTaskDTO updateById(int projectId, int taskId, UpdateTaskDTO updateTaskDTO, String login);

}

package org.example.service;

import org.example.dto.task.BaseTaskDTO;
import org.example.dto.task.CreateTaskDTO;
import org.example.dto.task.UpdateTaskDTO;
import org.example.entity.Task;

import java.util.List;

public interface TaskService {

    BaseTaskDTO save(int projectId, CreateTaskDTO createTaskDTO);

    BaseTaskDTO getById(int projectId, int TaskId);

    void deleteById(int projectId, int taskId);

    List<BaseTaskDTO> getAll(int projectId);

    BaseTaskDTO updateById(int projectId, int taskId, UpdateTaskDTO updateTaskDTO);

}

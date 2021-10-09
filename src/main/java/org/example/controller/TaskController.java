package org.example.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.dto.task.BaseTaskDTO;
import org.example.dto.task.CreateTaskDTO;
import org.example.dto.task.UpdateTaskDTO;
import org.example.service.TaskService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/project")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{projectId}/task")
    @ApiOperation("Get all Task by Project ID")
    public List<BaseTaskDTO> getAll(@PathVariable @NotNull int projectId) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return taskService.getAll(projectId, login);
    }

    @GetMapping("/{projectId}/task/{taskId}")
    @ApiOperation("Get Task by Project ID & Task ID")
    public BaseTaskDTO getById(@PathVariable @NotNull int projectId,
                               @PathVariable @NotNull int taskId) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return taskService.getById(projectId, taskId, login);
    }

    @PostMapping("/{projectId}/task")
    @ApiOperation("Add new task")
    public BaseTaskDTO addNew(@PathVariable(name = "projectId") @NotNull int projectId,
                              @RequestBody @NotNull CreateTaskDTO createTaskDTO) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return taskService.save(projectId, createTaskDTO, login);
    }

    @DeleteMapping("/{projectId}/task/{taskId}")
    @ApiOperation("Delete task by ID")
    void deleteById(@PathVariable(name = "projectId") @NotNull int projectId,
                    @PathVariable(name = "taskId") @NotNull int taskId) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        taskService.deleteById(projectId, taskId, login);
    }

    @PutMapping("/{projectId}/task/{taskId}")
    @ApiOperation("Update task By ID")
    public BaseTaskDTO updateTask(@PathVariable(name = "projectId") @NotNull int projectId,
                                  @PathVariable(name = "taskId") @NotNull int taskId,
                                  @RequestBody @NotNull UpdateTaskDTO updateTaskDTO) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return taskService.updateById(projectId, taskId, updateTaskDTO, login);
    }

}

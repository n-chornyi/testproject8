package org.example.mapper;

import org.example.dto.project.UpdateProjectDTO;
import org.example.dto.task.BaseTaskDTO;
import org.example.dto.task.CreateTaskDTO;
import org.example.dto.task.UpdateTaskDTO;
import org.example.entity.Project;
import org.example.entity.Task;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TasksMapper {

    BaseTaskDTO getDTO(Task task);

    Task parseDTO(BaseTaskDTO baseTaskDTO);

    Task parseDTO(CreateTaskDTO createTaskDTO);

    List<BaseTaskDTO> getAll(List<Task> tasks);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task update(UpdateTaskDTO updateTaskDTO, @MappingTarget Task task);
}

package org.example.mapper;

import org.example.dto.project.BaseProjectDTO;
import org.example.dto.project.CreateProjectDTO;
import org.example.dto.project.UpdateProjectDTO;
import org.example.entity.Project;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TasksMapper.class})
public interface ProjectMapper {

    BaseProjectDTO getDTO(Project project);

    Project parseDTO(BaseProjectDTO baseProject);

    Project parseDTO(CreateProjectDTO createProjectDTO);

    List<BaseProjectDTO> getAll(List<Project> projects);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Project update(UpdateProjectDTO updateProjectDTO, @MappingTarget Project project);

}

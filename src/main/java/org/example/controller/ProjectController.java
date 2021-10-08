package org.example.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.dto.project.BaseProjectDTO;
import org.example.dto.project.CreateProjectDTO;
import org.example.dto.project.UpdateProjectDTO;
import org.example.service.ProjectService;
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
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    @ApiOperation("Get all projects")
    public List<BaseProjectDTO> getAll() {
        return projectService.getAll();
    }

    @GetMapping("/{projectId}")
    @ApiOperation("Get project by id")
    public BaseProjectDTO getById(@PathVariable(name = "projectId") @NotNull int id) {
        return projectService.getById(id);
    }

    @PostMapping
    @ApiOperation("Add new project")
    public BaseProjectDTO addProject(@RequestBody @NotNull CreateProjectDTO project) {
        return projectService.save(project);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update project")
    public BaseProjectDTO updateById(@PathVariable(name = "id") @NotNull int id,
                            @RequestBody @NotNull UpdateProjectDTO updateProjectDTO) {
        return projectService.updateById(id, updateProjectDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete project")
    void deleteById(@PathVariable(name = "id") @NotNull int id) {
        projectService.deleteById(id);
    }
}

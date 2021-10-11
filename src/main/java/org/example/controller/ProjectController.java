package org.example.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.dto.project.BaseProjectDTO;
import org.example.dto.project.CreateProjectDTO;
import org.example.dto.project.UpdateProjectDTO;
import org.example.service.ProjectService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/project")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    @ApiOperation("Get all projects")
    public List<BaseProjectDTO> getAll() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return projectService.getAll(login);
    }

    @GetMapping("/{projectId}")
    @ApiOperation("Get project by id")
    public BaseProjectDTO getById(@PathVariable(name = "projectId") @NotNull int id) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return projectService.getById(id, login);
    }

    @PostMapping
    @ApiOperation("Add new project")
    public BaseProjectDTO addProject(@RequestBody @Valid @NotNull CreateProjectDTO project) {
        System.out.println(project.getName());
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return projectService.save(project, login);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update project")
    public BaseProjectDTO updateById(@PathVariable(name = "id") @NotNull int id,
                            @RequestBody @Valid @NotNull UpdateProjectDTO updateProjectDTO) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return projectService.updateById(id, updateProjectDTO, login);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete project")
    void deleteById(@PathVariable(name = "id") @NotNull int id) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        projectService.deleteById(id, login);
    }
}

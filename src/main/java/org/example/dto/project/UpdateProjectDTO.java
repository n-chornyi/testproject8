package org.example.dto.project;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateProjectDTO {

    @NotBlank(message = "Name shouldn't blank")
    @Size(min = 5, max = 255, message = "Length name {min}-{max} characters")
    private String name;
}

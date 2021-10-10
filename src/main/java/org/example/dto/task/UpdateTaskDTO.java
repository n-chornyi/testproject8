package org.example.dto.task;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateTaskDTO {

    @NotBlank(message = "title shouldn't blank")
    @Size(min = 5, max = 255, message = "Length title {min}-{max} characters")
    private String title;
    private boolean status;
}

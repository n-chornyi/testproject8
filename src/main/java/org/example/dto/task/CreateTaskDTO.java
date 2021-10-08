package org.example.dto.task;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateTaskDTO {

    @NotBlank(message = "Error! title should be not blank")
    private String title;
    private boolean status;
}

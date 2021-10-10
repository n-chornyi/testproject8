package org.example.dto.task;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CreateTaskDTO {
    @NotBlank(message = "title shouldn't blank")
    @Size(min = 5, max = 255, message = "Length title {min}-{max} characters")
    private String title;
    private boolean status;
}

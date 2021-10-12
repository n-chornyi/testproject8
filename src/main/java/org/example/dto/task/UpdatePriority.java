package org.example.dto.task;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdatePriority {

    @NotNull(message = "Indexes can't null")
    private String indexes;
}

package org.example.dto.task;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseTaskDTO {

    private int id;
    private String title;
    private boolean status;
    private LocalDateTime endTime;

}

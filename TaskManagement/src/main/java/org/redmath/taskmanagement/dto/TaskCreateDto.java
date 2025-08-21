package org.redmath.taskmanagement.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskCreateDto {
    private String title;
    private String description;
    private Boolean completed;
}

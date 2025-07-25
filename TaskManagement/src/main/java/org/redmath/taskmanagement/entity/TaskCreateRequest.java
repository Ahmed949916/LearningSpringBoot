package org.redmath.taskmanagement.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskCreateRequest {
    private String title;
    private String description;
}

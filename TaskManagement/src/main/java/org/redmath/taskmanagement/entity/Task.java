package org.redmath.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue
    private Long taskId;
    private String title;
    private String description;

    @Column(name = "owner_id")
    private Long ownerId;
}

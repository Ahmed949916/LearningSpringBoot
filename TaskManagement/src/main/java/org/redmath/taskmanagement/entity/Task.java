package org.redmath.taskmanagement.entity;

import jakarta.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}

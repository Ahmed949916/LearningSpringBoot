package org.redmath.taskmanagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;
    private String username;
    private String password;
    private String role;


}


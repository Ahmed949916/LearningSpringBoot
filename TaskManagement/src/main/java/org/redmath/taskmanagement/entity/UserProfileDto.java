package org.redmath.taskmanagement.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {
    private Users user;
    private String  photoLink;
}
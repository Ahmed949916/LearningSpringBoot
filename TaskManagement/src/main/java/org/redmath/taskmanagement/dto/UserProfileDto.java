package org.redmath.taskmanagement.dto;


import lombok.Getter;
import lombok.Setter;
import org.redmath.taskmanagement.entity.Users;

@Getter
@Setter
public class UserProfileDto {
    private Users user;
    private String  photoLink;
}
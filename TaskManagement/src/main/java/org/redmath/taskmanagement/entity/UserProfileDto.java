package org.redmath.taskmanagement.entity;

import org.redmath.taskmanagement.entity.Users;

public class UserProfileDto {
    private Users user;
    private String  photoLink;

    public UserProfileDto(Users user, String photoLink) {
        this.user = user;
        this.photoLink = photoLink;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }
}
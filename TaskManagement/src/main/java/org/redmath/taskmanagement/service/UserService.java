package org.redmath.taskmanagement.service;

import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public Users getUserById(Long id) {
        return userRepo.findById(id).orElseThrow();
    }

    public Users createUser(Users user) {
        return userRepo.save(user);
    }
}

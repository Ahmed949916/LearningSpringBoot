package org.redmath.taskmanagement.service;

import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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


    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteUser(Long id) {
        Users user = userRepo.findById(id).orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        userRepo.delete(user);
    }
}

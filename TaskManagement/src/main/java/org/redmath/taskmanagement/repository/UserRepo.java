package org.redmath.taskmanagement.repository;

import org.redmath.taskmanagement.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users,Long> {
    Optional<Users> findByUsername(String username);
}

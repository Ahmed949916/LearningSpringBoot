package org.redmath.taskmanagement.repository;

import org.redmath.taskmanagement.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users,Long> {

}

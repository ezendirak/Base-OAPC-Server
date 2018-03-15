package com.oapc.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oapc.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername( String username );
}


package com.oapc.service;

import java.util.List;

import com.oapc.model.User;

public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll ();
}

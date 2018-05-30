package com.oapc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oapc.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername( String username );
    
    @Query("select p from User p where p.empresa.codi =:nom")
    User findUserByName(@Param("nom") String nom);
}


package com.oapc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.oapc.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername( String username );
    
    @Query("select p from User p where p.empresa.codi =:nom")
    List<User> findUserByCodiEmp(@Param("nom") String nom);
    
//    @Query("select p from User p where p.id IN (select user_id from user_authority where authority_id IN (Select id from Authority where name=:rol))")
    @Query("select p from User p where p.empresa.codi !='Administració'")
    List<User> findAllUserRole();
    
}


package com.oapc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

import com.oapc.model.ErrorRegister;
import com.oapc.model.Register;


public interface ErrorRegisterRepository extends JpaRepository<Register, Long> {
	
	@Query("select p from Register p")
	List<ErrorRegister> findAllList();
	
	@Query("select p from Register p order by id")
	Stream<ErrorRegister> findAllStream();	

}
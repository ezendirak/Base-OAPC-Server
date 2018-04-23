package com.oapc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

import com.oapc.model.Register;


public interface RegisterRepository extends JpaRepository<Register, Long> {
	
	@Query("select p from Register p")
	List<Register> findAllList();
	
	@Query("select p from Register p order by id")
	Stream<Register> findAllStream();
	
//	@Query("select p from PDU p where p.tabla = :taula and p.clave like %:key%")
//	Stream<PDU> getDades(@Param("taula") String taula, @Param("key") String key);
	
}
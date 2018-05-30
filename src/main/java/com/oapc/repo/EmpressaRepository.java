package com.oapc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.stream.Stream;

import com.oapc.model.Empressa;


public interface EmpressaRepository extends JpaRepository<Empressa, Long> {
	
	@Query("select p from Empressa p")
	List<Empressa> findAllList();
	
	@Query("select p from Empressa p order by id")
	Stream<Empressa> findAllStream();
	
	@Query("select p from Empressa p where p.id = :id")
	Empressa findById(@Param("id") Long id);
	
	@Query("select p from Empressa p where p.codi = :codi")
	Empressa findByCodi(@Param("codi") String codi);
	
	@Query("select p from Empressa p where p.id in (select empressa from EmpressaProducte where tipusProducte = :producte)")
	List<Empressa> getEmpresesByProd(@Param("producte") String producte);
	
	@Query("select p from Empressa p where p.id = (select empresa from User x where x.id = :userId)")
	Empressa getCodiEmpByUserId(@Param("userId") Long userId);
}
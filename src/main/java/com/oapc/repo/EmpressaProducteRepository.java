package com.oapc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.stream.Stream;

import com.oapc.model.Empressa;
import com.oapc.model.EmpressaProducte;


public interface EmpressaProducteRepository extends JpaRepository<EmpressaProducte, Long> {
	
	@Query("select p from Empressa p")
	List<EmpressaProducte> findAllList();
	
	@Query("select p from Empressa p order by id")
	Stream<EmpressaProducte> findAllStream();
	
	@Query("select p from EmpressaProducte p where p.empressa = :empressaID")
	List<EmpressaProducte> findAllStreamByEmpressa(@Param("empressaID") Empressa empressaID);
	
	@Query("select p from EmpressaProducte p where p.tipusProducte = :producte and p.empressa = :empressaID")
	EmpressaProducte findAllListByProdAndEmpId(@Param("producte") String producte, @Param("empressaID") Empressa empressaID);
}
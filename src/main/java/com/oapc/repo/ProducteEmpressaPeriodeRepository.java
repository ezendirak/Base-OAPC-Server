package com.oapc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.stream.Stream;

import com.oapc.model.Empressa;
import com.oapc.model.EmpressaProducte;
import com.oapc.model.ProducteEmpressaPeriode;


public interface ProducteEmpressaPeriodeRepository extends JpaRepository<ProducteEmpressaPeriode, Long> {
	
	@Query("select p from ProducteEmpressaPeriode p")
	List<ProducteEmpressaPeriode> findAllList();
	
	@Query("select p from ProducteEmpressaPeriode p order by id")
	Stream<ProducteEmpressaPeriode> findAllStream();
	
	@Query("select x from ProducteEmpressaPeriode x where x.id IN (select id from EmpressaProducte where tipusProducte IN :productes)")
	List<ProducteEmpressaPeriode> findAllListByProd(@Param("productes") List<String> productes);
	
}
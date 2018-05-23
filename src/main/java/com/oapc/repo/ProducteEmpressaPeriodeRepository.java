package com.oapc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.stream.Stream;

import com.oapc.model.Empressa;
import com.oapc.model.EmpressaProducte;
import com.oapc.model.Periode;
import com.oapc.model.ProducteEmpressaPeriode;


public interface ProducteEmpressaPeriodeRepository extends JpaRepository<ProducteEmpressaPeriode, Long> {
	
	@Query("select p from ProducteEmpressaPeriode p")
	List<ProducteEmpressaPeriode> findAllList();
	
	@Query("select p from ProducteEmpressaPeriode p order by id")
	Stream<ProducteEmpressaPeriode> findAllStream();
	
//	@Query("select x from ProducteEmpressaPeriode x where x.id IN (select id from EmpressaProducte where tipusProducte IN :productes)")
//	List<ProducteEmpressaPeriode> findAllListByProd(@Param("productes") List<String> productes);
	
	@Query("select x from ProducteEmpressaPeriode x where x.tancat != 1 and x.noComercialitzacio != 1 and x.empressaProducte IN (select id from EmpressaProducte where tipusProducte IN :productes)")
	List<ProducteEmpressaPeriode> findAllListByProd(@Param("productes") List<String> productes);
	
	@Query("select x from ProducteEmpressaPeriode x where x.empressaProducte = (select id from EmpressaProducte where empressa = (select id from Empressa where codi = :codiProd))")
	ProducteEmpressaPeriode findByCodiEmp(@Param("codiProd") List<String> codiProd);
	
	@Query("select x from ProducteEmpressaPeriode x where x.periode = :periode AND x.empressaProducte = (select id from EmpressaProducte d where d.tipusProducte = :producte AND d.empressa = (select id from Empressa where codi = :codiProd))")
	ProducteEmpressaPeriode findByPerProdAndCodiEmp(@Param("periode") Periode periode, @Param("producte") String producte, @Param("codiProd") String codiProd);
	
//	@Query("select x from ProducteEmpressaPeriode x where ")
}
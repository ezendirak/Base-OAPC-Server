package com.oapc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import com.oapc.model.Periode;


public interface PeriodeRepository extends JpaRepository<Periode, Long> {
	
	@Query("select p from Periode p order by p.numPeriode")
	List<Periode> findAllList();
	
	@Query("select p from Periode p order by id")
	Stream<Periode> findAllStream();
	
//	Inicialment s'utilitzava la data actual per treure els periodes a partir d'avui. Ara treuren directament els periodes que tinguin duracio > 0.	
//	@Query("select p from Periode p where p.dataInici <= :formatedDate and p.dataFi >= :formatedDate order by p.numPeriode")
//	Stream<Periode> getDatesDisponibles(@Param("formatedDate") Date formatedDate);
	
	@Query("select p from Periode p where p.duracio > 0 and p.id IN (select DISTINCT(periode) from ProducteEmpressaPeriode o where o.pendent = true)")
	Stream<Periode> getDatesDisponibles();
	
	@Query("select p from Periode p where p.dataInici >= :formatedDate and tipusPeriode = :tipusProduct order by p.numPeriode")
	List<Periode> getDatesByProductAndDate(@Param("tipusProduct") String tipusProduct, @Param("formatedDate") Date formatedDate);
	
	@Query("select p from Periode p where p.numPeriode = :numPeriode and p.tipusPeriode = :tipusPeriode order by p.numPeriode")
	Periode findPeriodByNumType(@Param("numPeriode") Integer numPeriode, @Param("tipusPeriode") String tipusPeriode);
	
}
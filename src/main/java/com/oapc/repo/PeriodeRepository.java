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
	
	@Query("select p from Periode p")
	List<Periode> findAllList();
	
	@Query("select p from Periode p order by id")
	Stream<Periode> findAllStream();
	
	@Query("select p from Periode p where p.dataInici <= :formatedDate and p.dataFi >= :formatedDate")
	Stream<Periode> getDatesDisponibles(@Param("formatedDate") Date formatedDate);
	
	@Query("select p from Periode p where p.numPeriode = :numPeriode and p.tipusPeriode = :tipusPeriode")
	Periode findRepoByNumType(@Param("numPeriode") Integer numPeriode, @Param("tipusPeriode") String tipusPeriode);
	
}
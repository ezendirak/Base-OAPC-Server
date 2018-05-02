package com.oapc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

import com.oapc.model.PDU;

public interface PduRepository extends JpaRepository<PDU, Long> {
	
	@Query("select p from PDU p")
	List<PDU> findAllList();
	
	@Query("select p from PDU p order by id")
	Stream<PDU> findAllStream();
	
	@Query("select p from PDU p where p.tabla = :taula and p.clave like %:key%")
	Stream<PDU> getDades(@Param("taula") String taula, @Param("key") String key);
	
	@Query("select p from PDU p where p.tabla = :taula and p.datos like %:producte%")
	Stream<PDU> getProducteByDades(@Param("taula") String taula, @Param("producte") String producte);
	
	@Query("select p from PDU p where p.clave like %:clave% and p.datos = :valor")
	Stream<PDU> getIfOldProduct(@Param("clave") String clave, @Param("valor") String valor);
	
}
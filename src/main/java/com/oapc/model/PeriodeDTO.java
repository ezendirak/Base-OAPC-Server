package com.oapc.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.sql.Timestamp;
import java.util.*;


public class PeriodeDTO {
	
	@Id
    private Long id;

	private String tipusPeriode;
	
	private Integer any;
	
	private Integer numPeriode;
	
	private Date dataInici;
	
	private Date dataFi;
	
	private Integer duracio;
	
	
	public PeriodeDTO() {
		
	}
	
	public PeriodeDTO(String tipusPeriode, Integer any, Integer numPeriode, Date dataInici, Date dataFi, Integer duracio) {
		this.tipusPeriode = tipusPeriode;
		this.any = any;
		this.numPeriode = numPeriode;
		this.dataInici = dataInici;
		this.dataFi = dataFi;
		this.duracio = duracio;
	}
	
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipusPeriode() {
		return tipusPeriode;
	}

	public void setTipusPeriode(String tipusPeriode) {
		this.tipusPeriode = tipusPeriode;
	}

	public Integer getAny() {
		return any;
	}

	public void setAny(Integer any) {
		this.any = any;
	}

	public Integer getNumPeriode() {
		return numPeriode;
	}

	public void setNumPeriode(Integer numPeriode) {
		this.numPeriode = numPeriode;
	}

	public Date getDataInici() {
		return dataInici;
	}

	public void setData_inici(Date dataInici) {
		this.dataInici = dataInici;
	}

	public Date getDataFi() {
		return dataFi;
	}

	public void setDataFi(Date dataFi) {
		this.dataFi = dataFi;
	}

	public Integer getDuracio() {
		return duracio;
	}

	public void setDuracio(Integer duracio) {
		this.duracio = duracio;
	}
	        
}
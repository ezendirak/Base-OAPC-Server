package com.oapc.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "periode")

public class Periode {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private String tipusPeriode;
	
	private Integer any;
	
	private Integer numPeriode;
	
	private Date dataInici;
	
	private Date dataFi;
	
	private Integer duracio;
	
	
	
//	@OneToMany(mappedBy = "periode", cascade = CascadeType.ALL)
//	@JsonBackReference
//	@JsonManagedReference
//	@JsonIgnoreProperties("registers")
//	@JsonIgnore
	
	@OneToMany(mappedBy = "periode")
	private Set<Register> registers = new HashSet();
    
	@OneToMany(mappedBy = "periode")
	private Set<ErrorRegister> errorRegisters = new HashSet();
	
	@JsonManagedReference
	@OneToMany(mappedBy = "periode")
	private Set<ProducteEmpressaPeriode> producteEmpressaPeriode = new HashSet();
	
	public Periode() {
		
	}
	
	public Periode(String tipusPeriode, Integer any, Integer numPeriode, Date dataInici, Date dataFi, Integer duracio) {
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
	
	
	public Set<Register> getRegisters() {
		return registers;
	}

	public void setRegisters(Set<Register> registers) {
		this.registers = registers;
	}
	
	public Set<ErrorRegister> getErrorRegisters() {
		return errorRegisters;
	}

	public void setErrorRegisters(Set<ErrorRegister> registers) {
		this.errorRegisters = registers;
	}
	
	public Set<ProducteEmpressaPeriode> getProducteEmpressaPeriode() {
		return producteEmpressaPeriode;
	}

	public void setProducteEmpressaPeriode(Set<ProducteEmpressaPeriode> producteEmpressaPeriode) {
		this.producteEmpressaPeriode = producteEmpressaPeriode;
	}
	
	

        
}
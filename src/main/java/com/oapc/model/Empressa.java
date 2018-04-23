package com.oapc.model;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "empressa")
public class Empressa {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private String codi;
	
	private Integer estat;

	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "empressa")
	private Set<EmpressaProducte> empressaProducte = new HashSet();
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "periode")
//	private Set<ProducteEmpressaPeriode> producteEmpressaPeriode = new HashSet();
	@JsonManagedReference
	@OneToMany(mappedBy = "empressa")
	private Set<Register> register = new HashSet();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "empressa")
	private Set<ErrorRegister> errorRegister = new HashSet();
	
	public Empressa() {
		
	}
	
	public Empressa(String codi, Integer estat) {
		this.codi = codi;
		this.estat = estat;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCodi() {
		return codi;
	}

	public void setCodi(String codi) {
		this.codi = codi;
	}

	public Integer getEstat() {
		return estat;
	}

	public void setEstat(Integer estat) {
		this.estat = estat;
	}
	
	public Set<EmpressaProducte> getEmpressaProducte() {
		return empressaProducte;
	}

	public void setEmpressaProducte(Set<EmpressaProducte> empressaProducte) {
		this.empressaProducte = empressaProducte;
	}
	
	public Set<Register> getRegisters() {
		return register;
	}

	public void setRegisters(Set<Register> register) {
		this.register = register;
	}
	
	public Set<ErrorRegister> getErrorRegister() {
		return errorRegister;
	}

	public void setErrorRegister(Set<ErrorRegister> register) {
		this.errorRegister = register;
	}
}
        

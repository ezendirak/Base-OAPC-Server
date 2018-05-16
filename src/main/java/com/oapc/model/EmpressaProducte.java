package com.oapc.model;

import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "empressaProducte")
public class EmpressaProducte {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private String tipusProducte;

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "empressa")
//	private Set<EmpressaProducte> empressaProducte = new HashSet();
	
	@JsonManagedReference("empPro")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "empressaProducte")
	private Set<ProducteEmpressaPeriode> producteEmpressaPeriode = new HashSet();
	
	
	@ManyToOne
	@JoinColumn(name="empressa_id", nullable = false)
//	@JoinTable(	name="empressa_id", 
//				joinColumns= @JoinColumn(name="", referencedColumnName = "id"),
//				inverseJoinColumns = @JoinColumn(name = "", referencedColumnName = "id"))
	private Empressa empressa;
	public Long getId() {
		
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipusProducte() {
		return tipusProducte;
	}

	public void setTipusProducte(String tipusProducte) {
		this.tipusProducte = tipusProducte;
	}
	
	public Empressa getEmpressa() {
		return empressa;
	}
	
	public void setEmpressa(Empressa empressa) {
		this.empressa = empressa;
	}
	
	
	
}
        

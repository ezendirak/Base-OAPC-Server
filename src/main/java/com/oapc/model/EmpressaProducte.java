package com.oapc.model;

import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "empressaProducte")
public class EmpressaProducte {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private String tipusProducte;

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "empressa")
//	private Set<EmpressaProducte> empressaProducte = new HashSet();
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "empressaProducte")
	private ProducteEmpressaPeriode producteEmpressaPeriode;
	
	
	@ManyToOne
	@JoinColumn(name="empressa_id", nullable = false)
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
        

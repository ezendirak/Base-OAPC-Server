package com.oapc.model;

import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.glass.ui.View;

@Entity
@Table(name = "producteEmpressaPeriode")
public class ProducteEmpressaPeriode {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private Timestamp dataUltimRegistre;
	
	private Boolean pendent;
	
	private Boolean registrat;
	
	private Boolean noComercialitzacio;
	
	private Boolean tancat;
	

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="periode_id", nullable = false)
	private Periode periode;
	
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="empressaProduct_id", nullable = false)
	private EmpressaProducte empressaProducte;
	
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Periode getIdPeriode() {
		return periode;
	}

	public void setIdPeriode(Periode periode) {
		this.periode = periode;
	}

	public Timestamp getDataUltimRegistre() {
		return dataUltimRegistre;
	}

	public void setDataUltimRegistre(Timestamp dataUltimRegistre) {
		this.dataUltimRegistre = dataUltimRegistre;
	}

	public Boolean getPendent() {
		return pendent;
	}

	public void setPendent(Boolean pendent) {
		this.pendent = pendent;
	}

	public Boolean getRegistrat() {
		return registrat;
	}

	public void setRegistrat(Boolean registrat) {
		this.registrat = registrat;
	}

	public Boolean getNoComercialitzacio() {
		return noComercialitzacio;
	}

	public void setNo_comercialitzacio(Boolean noComercialitzacio) {
		this.noComercialitzacio = noComercialitzacio;
	}

	public Boolean getTancat() {
		return tancat;
	}

	public void setTancat(Boolean tancat) {
		this.tancat = tancat;
	}
	
	public EmpressaProducte getEmpressaProducte() {
		return empressaProducte;
	}
	
	public void setEmpressaProducte(EmpressaProducte empressaProducte) {
		this.empressaProducte = empressaProducte;
	}
	
}
package com.oapc.model;

import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class PepDTO {
	
    private Long id;

	private Timestamp dataUltimReg;
	
	private Boolean pendent;
	
	private Boolean registrat;
	
	private Boolean noComercialitzacio;
	
	private Boolean tancat;
	
	private String periode;
	
	private String tipusProducte;
	
	private String codiEmpresa;
	
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdPeriode() {
		return periode;
	}

	public void setIdPeriode(String periode) {
		this.periode = periode;
	}

	public Timestamp getDataUltimRegistre() {
		return dataUltimReg;
	}

	public void setDataUltimRegistre(Timestamp dataUltimRegistre) {
		this.dataUltimReg = dataUltimRegistre;
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
	
	public String getEmpressaProducte() {
		return tipusProducte;
	}
	
	public void setEmpressaProducte(String empressaProducte) {
		this.tipusProducte = empressaProducte;
	}

	public String getCodiEmpresa() {
		return codiEmpresa;
	}

	public void setCodiEmpresa(String codiEmpresa) {
		this.codiEmpresa = codiEmpresa;
	}
	
}
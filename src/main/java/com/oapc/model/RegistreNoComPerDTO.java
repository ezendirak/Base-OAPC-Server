package com.oapc.model;

import javax.persistence.*;

import java.util.*;


public class RegistreNoComPerDTO {
	
	@Id
    private Long id;

	private String producte;
	
	private String empresa;
	
	private Long periode;
	
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProducte() {
		return producte;
	}

	public void setProducte(String producte) {
		this.producte = producte;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public Long getPeriode() {
		return periode;
	}

	public void setPeriode(Long periode) {
		this.periode = periode;
	}

	
	        
}
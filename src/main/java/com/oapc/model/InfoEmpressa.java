package com.oapc.model;

import java.util.*;


public class InfoEmpressa {
	
	
    private String codi;

	private List<String> tipusProductes;
	
	private Integer estat;
	
	
	public InfoEmpressa() {
		
	}


	public String getCodi() {
		return codi;
	}


	public void setCodi(String codi) {
		this.codi = codi;
	}


	public List<String> getTipusProductes() {
		return tipusProductes;
	}


	public void setTipusProductes(List<String> tipusProductes) {
		this.tipusProductes = tipusProductes;
	}


	public void setToListTipusProductes(String tipusProducte) {
		this.tipusProductes.add(tipusProducte);
	}
	
	public Integer getEstat() {
		return estat;
	}


	public void setEstat(Integer estat) {
		this.estat = estat;
	}
	
	
	        
}
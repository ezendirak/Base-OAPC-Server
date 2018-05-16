package com.oapc.model;

import java.util.*;


public class InfoEmpressa2 {
	
	
    private String codi;

	private List<String> tipusProductes;
	
	private Estat estat;
	
	
	public InfoEmpressa2() {
		
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
	
	public Estat getEstat() {
		return estat;
	}


	public void setEstat(Estat estat) {
		this.estat = estat;
	}
	
	
	        
}
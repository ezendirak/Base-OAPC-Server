package com.oapc.model;

import java.util.Date;

public class InfoGestioReg {

    private String periode;
    
    private String codiEmpressa;
    
    private String tipusProducte;
    
    private Date dataUltimReg;
    
    private Boolean pendent;
    
    private Boolean registrat;
    
    private Boolean noComercialitzacio;
    
    private Boolean tancat;

	public String getPeriode() {
		return periode;
	}

	public void setPeriode(String periode) {
		this.periode = periode;
	}

	public String getCodiEmpressa() {
		return codiEmpressa;
	}

	public void setCodiEmpressa(String codiEmpressa) {
		this.codiEmpressa = codiEmpressa;
	}

	public String getTipusProducte() {
		return tipusProducte;
	}

	public void setTipusProducte(String tipusProducte) {
		this.tipusProducte = tipusProducte;
	}

	public Date getDataUltimReg() {
		return dataUltimReg;
	}

	public void setDataUltimReg(Date dataUltimReg) {
		this.dataUltimReg = dataUltimReg;
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

	public void setNoComercialitzacio(Boolean noComercialitzacio) {
		this.noComercialitzacio = noComercialitzacio;
	}

	public Boolean getTancat() {
		return tancat;
	}

	public void setTancat(Boolean tancat) {
		this.tancat = tancat;
	}
    
}
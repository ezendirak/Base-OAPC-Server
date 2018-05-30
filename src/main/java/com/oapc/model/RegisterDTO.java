package com.oapc.model;

import javax.persistence.*;




public class RegisterDTO {
	
    private Long id;

//	@Column(name="TABLA", length=10)
    private PeriodeDTO  periode;

	
//	@Column(name="CLAVE", length=30)
    private String  tipusProducte;
	
//	@Column(name="CONT")
    private String varietat;    
    
//    @Column(name="DATOS", length=200)
    private String  colorCarn;

//    @Column(name="CONT")
    private String qualitat;
    
//  @Column(name="CONT")
    private String calibre;
  
//@Column(name="CONT")
    private Long quantitatVenuda;
    
//  @Column(name="CONT")
    private Float preuSortida;
    
    private String usuName;
    
    public RegisterDTO() {}
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public PeriodeDTO getPeriode() {
		return periode;
	}

	public void setPeriode(PeriodeDTO periode) {
		this.periode = periode;
	}

	public String getTipusProducte() {
		return tipusProducte;
	}

	public void setTipusProducte(String tipusProducte) {
		this.tipusProducte = tipusProducte;
	}

	public String getVarietat() {
		return varietat;
	}

	public void setVarietat(String varietat) {
		this.varietat = varietat;
	}

	public String getColorCarn() {
		return colorCarn;
	}

	public void setColorCarn(String colorCarn) {
		this.colorCarn = colorCarn;
	}

	public String getQualitat() {
		return qualitat;
	}

	public void setQualitat(String qualitat) {
		this.qualitat = qualitat;
	}

	public String getCalibre() {
		return calibre;
	}

	public void setCalibre(String calibre) {
		this.calibre = calibre;
	}

	public Long getQuantitatVenuda() {
		return quantitatVenuda;
	}

	public void setQuantitatVenuda(Long quantitatVenuda) {
		this.quantitatVenuda = quantitatVenuda;
	}
	
	public Float getPreuSortida() {
		return preuSortida;
	}

	public void setPreuSortida(Float preuSortida) {
		this.preuSortida = preuSortida;
	}

	public String getUsuName() {
		return usuName;
	}

	public void setUsuName(String usuName) {
		this.usuName = usuName;
	}

        
}
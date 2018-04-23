package com.oapc.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "register")

public class Register {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//	@Column(name="TABLA", length=10)
//    private String  periode;
	
	@ManyToOne
	@JoinColumn(name="idPeriode", nullable = false)
//	@JsonManagedReference
//	@JsonBackReference
	@JsonIgnoreProperties("registers")
//	@JoinTable(name="register_periode")
//	@JsonManagedReference
	private Periode periode;
	
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
    
    
    @JsonBackReference
    @ManyToOne
	@JoinColumn(name="empresaInformant", nullable = false)
    private Empressa empressa;
    
    public Register() {}
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
//	@JsonIgnore
	
	public Periode getPeriode() {
		return periode;
	}

	public void setPeriode(Periode periode) {
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

	public Empressa getEmpressa() {
		return empressa;
	}

	public void setEmpressa(Empressa empressa) {
		this.empressa = empressa;
	}
	
	public Float getPreuSortida() {
		return preuSortida;
	}

	public void setPreuSortida(Float preuSortida) {
		this.preuSortida = preuSortida;
	}



        
}
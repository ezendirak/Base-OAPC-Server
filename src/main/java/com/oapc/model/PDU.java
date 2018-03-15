package com.oapc.model;

import javax.persistence.*;

@Entity
@Table(name = "pdu", indexes = { @Index (name = "PDU_IDX", columnList = "TABLA, CLAVE, CONT" )})
public class PDU {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column(name="TABLA", length=10)
    private String  tabla;
	
	@Column(name="CLAVE", length=30)
    private String  clave;
	
	@Column(name="CONT")
    private Integer cont;    
    
	//Per la taula PRODUCTES, l'estructura de dades es: 0-24 => Nom del producte, 25-26 => Familia, 27-29 => SubFamilia, 30-31 => Grup, 32-33 => Subgrup
	//Exemple pel pressec, camp datos => "Pressec                  PAPAGFRPI", Producte Agrari => Agricola => Fruita => Pinyol => Préssec.
    @Column(name="DATOS", length=200)
    private String  datos;

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Integer getCont() {
		return cont;
	}

	public void setCont(Integer cont) {
		this.cont = cont;
	}

	public String getDatos() {
		return datos;
	}

	public void setDatos(String datos) {
		this.datos = datos;
	}
        
}
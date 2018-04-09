package com.oapc.model;

import javax.persistence.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Entity
//@Table(name = "atributsCombo")
public class InfoRegistres {

//    private List<String> ColorsCarn;
    private String Clau;

    private String Nom;
    
    private String SubGrup;

	public String getClau() {
		return Clau;
	}

	public void setClau(String clau) {
		Clau = clau;
	}

	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}

	public String getSubGrup() {
		return SubGrup;
	}

	public void setSubGrup(String subGrup) {
		SubGrup = subGrup;
	}

    

}
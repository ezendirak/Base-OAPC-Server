package com.oapc.model;

public class InfoGestioProd {

//    private List<String> ColorsCarn;
    private String Producte;

    private String Atribut;
    
    private String Valor;
    
    private Integer Cont;
    
    private String SubGrup;
    
    private Long idProducte;
    
    private Long idRegistre;

	public String getProducte() {
		return Producte;
	}

	public void setProducte(String producte) {
		Producte = producte;
	}

	public String getAtribut() {
		return Atribut;
	}

	public void setAtribut(String atribut) {
		Atribut = atribut;
	}

	public String getValor() {
		return Valor;
	}

	public void setValor(String valor) {
		Valor = valor;
	}

	public Integer getCont() {
		return Cont;
	}

	public void setCont(Integer cont) {
		Cont = cont;
	}

	public String getSubGrup() {
		return SubGrup;
	}

	public void setSubGrup(String subGrup) {
		SubGrup = subGrup;
	}

	public Long getIdProducte() {
		return idProducte;
	}

	public void setIdProducte(Long idProducte) {
		this.idProducte = idProducte;
	}

	public Long getIdRegistre() {
		return idRegistre;
	}

	public void setIdRegistre(Long idRegistre) {
		this.idRegistre = idRegistre;
	}

	

    

}
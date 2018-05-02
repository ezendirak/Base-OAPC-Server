package com.oapc.model;

public class NewProdDTO {
	

	private String producte;
	
	private String taula;
	
	private Long idProd;
	
	private String valor;
	
	
	public NewProdDTO() {
		
	}
	
	public NewProdDTO(String producte, String taula, Long idProd, String valor) {
		this.producte = producte;
		this.taula = taula;
		this.idProd = idProd;
		this.valor = valor;
	}

	public String getProducte() {
		return producte;
	}

	public void setProducte(String producte) {
		this.producte = producte;
	}

	public String getTaula() {
		return taula;
	}

	public void setTaula(String taula) {
		this.taula = taula;
	}

	public Long getIdProd() {
		return idProd;
	}

	public void setIdProd(Long idProd) {
		this.idProd = idProd;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
    
	        
}
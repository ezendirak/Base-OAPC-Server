package com.oapc.model;

import javax.persistence.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Entity
//@Table(name = "atributsCombo")
public class AtributsCombo {

//    private List<String> ColorsCarn;
    private List<InfoRegistres> ColorsCarn;

    private List<InfoRegistres> Qualitats;
    
    private List<InfoRegistres> Calibres;
    
    private List<InfoRegistres> Varietats;

	public List<InfoRegistres> getColorsCarn() {
		return ColorsCarn;
	}

	public void setColorsCarn(List<InfoRegistres> colorsCarn) {
		ColorsCarn = colorsCarn;
	}

	public List<InfoRegistres> getQualitats() {
		return Qualitats;
	}

	public void setQualitats(List<InfoRegistres> qualitats) {
		Qualitats = qualitats;
	}

	public List<InfoRegistres> getCalibres() {
		return Calibres;
	}

	public void setCalibres(List<InfoRegistres> calibres) {
		Calibres = calibres;
	}

	public List<InfoRegistres> getVarietats() {
		return Varietats;
	}

	public void setVarietats(List<InfoRegistres> varietats) {
		Varietats = varietats;
	}


	
    

}
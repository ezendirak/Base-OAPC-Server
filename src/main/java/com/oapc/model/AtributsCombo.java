package com.oapc.model;

import javax.persistence.*;
import java.util.List;

//@Entity
//@Table(name = "atributsCombo")
public class AtributsCombo {

    private List<String> ColorsCarn;

    private List<String> Qualitats;
    
    private List<String> Calibres;
    
    private List<String> Varietats;

	public List<String> getColorsCarn() {
		return ColorsCarn;
	}

	public void setColorsCarn(List<String> colorsCarn) {
		ColorsCarn = colorsCarn;
	}

	public List<String> getQualitats() {
		return Qualitats;
	}

	public void setQualitats(List<String> qualitats) {
		Qualitats = qualitats;
	}

	public List<String> getCalibres() {
		return Calibres;
	}

	public void setCalibres(List<String> calibres) {
		Calibres = calibres;
	}

	public List<String> getVarietats() {
		return Varietats;
	}

	public void setVarietats(List<String> varietats) {
		Varietats = varietats;
	}
    

}
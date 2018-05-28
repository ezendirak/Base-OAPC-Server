package com.oapc.rest.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oapc.model.ErrorRest;
import com.oapc.model.InfoGestioProd;
import com.oapc.model.InfoRegistres;
import com.oapc.model.PDU;
import com.oapc.model.Register;
import com.oapc.repo.EmpressaRepository;
import com.oapc.repo.PduRepository;
import com.oapc.repo.PeriodeRepository;
import com.oapc.repo.RegisterRepository;
import com.oapc.rest.v2.PduController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api/v5")
public class GestioProdController {

	private String[] combos = {"COLORCARN", "VARIETAT", "QUALITAT", "CALIBRE"};
	
	private final Logger logger = LoggerFactory.getLogger(GestioProdController.class);
	
    @Autowired
    RegisterRepository registreRepository;
    
    @Autowired
    PduRepository pduRepository;
    
    @Autowired
    PeriodeRepository periodeRepository;
    
    @Autowired
    PduController pduController;
    
    @Autowired
    EmpressaRepository empressaRepository;

   
    
    public List<InfoGestioProd> filtrarAtributs(String tipusProducte, String familia, List<InfoGestioProd> registresTotals){
    	
    	registresTotals = registresTotals.stream()
    			  .filter(x -> tipusProducte == null || x.getProducte().equals(tipusProducte))
	              .filter(x -> familia     == null || x.getSubGrup().equals(familia)) //SubGrup PI - LL, familia: Pinyol - Llavor
	              .collect(Collectors.toList());
    	
	 return registresTotals;
    }
    
    
    @Transactional(readOnly = true)
    @GetMapping("/atributsProd")
    @PreAuthorize("hasRole('GESTOR')")
    public List<InfoGestioProd> getAllAtributsFromProds()	    		    		    	
    {    
    	List<InfoGestioProd> regiToGestioProd = new ArrayList<InfoGestioProd>();
    	
    	List<InfoRegistres> productes = getProductsTrueNamesAndKey();
    	
    	for (InfoRegistres prod : productes) {
			for (String atribut : combos) {
				Stream<PDU> listDades = pduRepository.getDades(atribut, prod.getClau());
				//DTO OMPLIR
				for (PDU registrePDU : listDades.collect(Collectors.toList())) {
					InfoGestioProd registreProd = new InfoGestioProd();
					registreProd.setProducte(prod.getNom());
					registreProd.setAtribut(atribut);
					registreProd.setValor(registrePDU.getDatos());
					registreProd.setCont(registrePDU.getCont());
					registreProd.setSubGrup(prod.getSubGrup());
					registreProd.setIdProducte(prod.getId());
					registreProd.setIdRegistre(registrePDU.getId());
					 
					regiToGestioProd.add(registreProd);
				}
			}
		}
    	 return regiToGestioProd;
    }
    
  
    @Transactional(readOnly = true)
    public List<InfoRegistres> getProductsTrueNamesAndKey(){
    	String vacio = "";
    	Stream<PDU> pduStream = pduRepository.getDades("PRODUCTE", vacio);
    	List<InfoRegistres> productes = new ArrayList<InfoRegistres>();
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
    		InfoRegistres temp = new InfoRegistres();
    		temp.setClau(registre.getClave());
    		temp.setNom(registre.getDatos().substring(0, 25).trim());
    		temp.setSubGrup(registre.getDatos().substring(32, 34).trim());
    		temp.setId(registre.getId());
//			productes.add(registre.getDatos().substring(0, 25).trim());
////			productes.add(registre.getClave());
    		productes.add(temp);
		}
    	
    	return productes;
    }
    
    
    @PutMapping("/gestioProducte")
    @PreAuthorize("hasRole('GESTOR')")
    public ResponseEntity<PDU> updateRegister(@Valid @RequestBody InfoGestioProd registerDetails) {
    	
//    	Register registre = registreRepository.findOne(registerDetails.getId());
    	PDU registre = pduRepository.findOne(registerDetails.getIdProducte());
    	PDU updatedRegistre = new PDU();
    	if ((registerDetails.getProducte() != null) && registre.getDatos().substring(0, 25).trim() != registerDetails.getProducte()) {
    		//HEM MODIFICAT EL NOM DEL PRODUCTE
    		if (registerDetails.getProducte().length() < 26) {
    			String temporal = registre.getDatos().substring(26, 34);
    			String newProduct = registerDetails.getProducte();
    			String newProduct2 = newProduct;
    			while(newProduct.length() < 26) {
    				newProduct = newProduct + " ";
    			}
    			newProduct = newProduct + temporal;
    			actualitzarRegistres(registre.getDatos().substring(0, 25).trim(), newProduct2, 1, null);
    			registre.setDatos(newProduct);
    			//Guarda
    			updatedRegistre = pduRepository.save(registre);
//    			pduController.getProductsName2();
    		}else {
    			logger.info("Nou Producte major a 25 caracters (maxim)");
    		}
    	}
    	PDU registre2 = pduRepository.findOne(registerDetails.getIdRegistre());
    	if ((registerDetails.getValor() != null) && registre2.getDatos().equals(registerDetails.getValor())) {
    		//HEM MODIFICAT EL VALOR D'UN ATRIBUT
    		
    		actualitzarRegistres(registre2.getDatos(), registerDetails.getValor(), 2, registre.getDatos().substring(0, 25).trim());
    		registre2.setDatos(registerDetails.getValor());
    		updatedRegistre = pduRepository.save(registre2);
    	}
//        Register updatedRegistre = registreRepository.save(registre);
        return ResponseEntity.ok(updatedRegistre);
    }
    
    
    public void actualitzarRegistres(String valorAntic, String valorNou, Integer taula, String producte) {
    	//Si no tenim cap registre amb aquest nom de producte, es un nom nou
    	List<Register> registres = registreRepository.findAll();
    	
    	for (Register register : registres) {
    		
    		if (taula == 1) {
    			
    			if (register.getTipusProducte().equals(valorAntic)) {
    				register.setTipusProducte(valorNou);
    				registreRepository.save(register);
    			}
    		}else if (taula == 2) {
    			
    			if (register.getCalibre().equals(valorAntic) && register.getTipusProducte().equals(producte)) {
    				register.setCalibre(valorNou);
    				registreRepository.save(register);
    				
    			}else if (register.getColorCarn().equals(valorAntic) && register.getTipusProducte().equals(producte)) {
    				register.setColorCarn(valorNou);
    				registreRepository.save(register);
    				
    			}else if (register.getQualitat().equals(valorAntic) && register.getTipusProducte().equals(producte)) {
    				register.setQualitat(valorNou);
    				registreRepository.save(register);
    			
    			}else if (register.getVarietat().equals(valorAntic) && register.getTipusProducte().equals(producte)) {
    				register.setVarietat(valorNou);
    				registreRepository.save(register);
    			}
    		}
		}
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////PAGINATION////////////////////////////////////////////////////
    
    @Transactional(readOnly = true)
    @GetMapping("/atributsProdFiltrat")
    @PreAuthorize("hasRole('GESTOR')")
    public ResponseEntity<?> getRegistresFiltratsPaginats(@RequestParam(value="page",     defaultValue="0") String spage,
    		@RequestParam(value="per_page", defaultValue="0") String sper_page,@RequestParam(value = "familia", required=false) String familia, @RequestParam(value="tipusProducte", required=false) String tipusProducte)	    		    		    	
    {    	    	 
   	
    	 List<InfoGestioProd> registresTotals = getAllAtributsFromProds();
    	 registresTotals = filtrarAtributs(tipusProducte, familia, registresTotals);
    	 
    	 
    	 Integer page      = Integer.parseInt(spage);
   	 	 Integer per_page  = Integer.parseInt(sper_page);
   	 	 
	     if (page == 0)
	    	 return new ResponseEntity<ErrorRest> (new ErrorRest("page not defined"), HttpStatus.BAD_REQUEST);
   	 	     	    	
	     if (per_page == 0)
	    	 return new ResponseEntity<ErrorRest> (new ErrorRest("per_page not defined"), HttpStatus.BAD_REQUEST);	     

    	 Long    total_reg = registresTotals.stream().count();
    	 Long    page_max  = (total_reg / per_page) + 1;
    	 Integer skip_reg  = (page - 1) * per_page;    	 
	     	     	     	     
	     if (page > page_max)
	    	 return new ResponseEntity<ErrorRest > (new ErrorRest("page > page_max"), HttpStatus.BAD_REQUEST);
    	
  	     	     	
	     
	     logger.info("page=" + page.toString() + ",page_max=" + page_max.toString() + ",per_page=" + per_page.toString() + ",total_reg=" +  total_reg.toString());	    
	   
	     if (page == 0)	    	
	    	 return new ResponseEntity<List<InfoGestioProd>> (registresTotals.stream().collect(Collectors.toList()), HttpStatus.OK); 
	     else 
		     return new ResponseEntity<List<InfoGestioProd>> (registresTotals.stream().skip(skip_reg).limit(per_page).collect(Collectors.toList()), HttpStatus.OK);
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/atributsProd_countFiltrat")
    @PreAuthorize("hasRole('GESTOR')")
    public Long getRegisterCountFiltrat(@RequestParam(value="tipusProducte", required=false) String tipusProducte, @RequestParam(value="familia", required=false) String familia)	    		    		    	
    {    
    	
    	 List<InfoGestioProd> registresTotals = getAllAtributsFromProds();
    	 
    	 registresTotals = filtrarAtributs(tipusProducte, familia, registresTotals);
    	
    	 return registresTotals.stream().count();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
}
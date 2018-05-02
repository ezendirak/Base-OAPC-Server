package com.oapc.rest.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oapc.model.Empressa;
import com.oapc.model.EmpressaProducte;
import com.oapc.model.ErrorRegister;
import com.oapc.model.ErrorRest;
import com.oapc.model.InfoEmpressa;
import com.oapc.model.InfoGestioProd;
import com.oapc.model.InfoRegistres;
import com.oapc.model.PDU;
import com.oapc.model.Periode;
import com.oapc.model.PeriodeDTO;
import com.oapc.model.Register;
import com.oapc.model.RegisterDTO;
import com.oapc.repo.EmpressaProducteRepository;
import com.oapc.repo.EmpressaRepository;
import com.oapc.repo.PduRepository;
import com.oapc.repo.PeriodeRepository;
import com.oapc.repo.RegisterRepository;
import com.oapc.rest.v2.PduController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api/v5")
public class GestioEmpressaController {

//	private String[] combos = {"COLORCARN", "VARIETAT", "QUALITAT", "CALIBRE"};
	
	private final Logger logger = LoggerFactory.getLogger(GestioEmpressaController.class);
	
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

    @Autowired
    EmpressaProducteRepository empressaProducteRepository;
   
    
    
    @Transactional(readOnly = true)
    @GetMapping("/allEmpresses")
    public List<String> getEmpressesName(){
    	
    	List<Empressa> empressesList = empressaRepository.findAll();
    	List<String> empList = new ArrayList<String>();
    	empList.add("Totes");
    	for (Empressa empressa : empressesList) {
			empList.add(empressa.getCodi());
		}
    	return empList;
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/productesModal")
    public List<InfoRegistres> getProductsName2(){
    	String vacio = "";
    	Stream<PDU> pduStream = pduRepository.getDades("PRODUCTE", vacio);
    	InfoRegistres producte = new InfoRegistres();
    	List<InfoRegistres> productes = new ArrayList<InfoRegistres>();
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
    		
    		producte = new InfoRegistres();
    		producte.setClau(registre.getClave());
    		producte.setNom(registre.getDatos().substring(0, 25).trim());
    		producte.setSubGrup(registre.getDatos().substring(32, 34).trim());
    		productes.add(producte);
		}
    	return productes;
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/nomProductesModal")
    public List<String> getProductsName3(){
    	String vacio = "";
    	Stream<PDU> pduStream = pduRepository.getDades("PRODUCTE", vacio);
    	
    	List<String> productes = new ArrayList<String>();
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
    		productes.add(registre.getDatos().substring(0, 25).trim());
		}
    	return productes;
    }
    
    public List<InfoEmpressa> filtrarAtributs(String tipusProducte, String estat, String codiEmpressa, List<InfoEmpressa> registresTotals){
    	
    	registresTotals = registresTotals.stream()
    			  .filter(x -> tipusProducte == null || x.getTipusProductes().contains(tipusProducte))
	              .filter(x -> estat     == null || x.getEstat().equals(Integer.valueOf(estat)))
	              .filter(x -> codiEmpressa == null || x.getCodi().equals(codiEmpressa))
	              .collect(Collectors.toList());
    	
	 return registresTotals;
    }
    
    @Transactional(readOnly = false)
    @PutMapping("/gestioEmpressa")
    public ResponseEntity<Empressa> updateEmpressa(@Valid @RequestBody InfoEmpressa empressa) {

    	Empressa emp = empressaRepository.findByCodi(empressa.getCodi());
    	
        if(emp == null) {
            return ResponseEntity.notFound().build();
        }
        if(emp.getEstat() != Integer.valueOf(empressa.getEstat())) {
        	emp.setEstat(Integer.valueOf(empressa.getEstat()));
        }
        
        for (String prod : empressa.getTipusProductes()) {
        	EmpressaProducte temp = empressaProducteRepository.findAllListByProdAndEmpId(prod, emp.getId());
        	if(temp == null) {
        		temp.setTipusProducte(prod);
        		temp.setEmpressa(emp);
        		EmpressaProducte updateEmpressaProd = empressaProducteRepository.save(temp);
        	}
		}
        
        Empressa updatedEmpressa = empressaRepository.save(emp);
        return ResponseEntity.ok(updatedEmpressa);
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////PAGINATION////////////////////////////////////////////////////
    
    @Transactional(readOnly = true)
    @GetMapping("/empressesFiltrat")
    public ResponseEntity<?> getRegistresFiltratsPaginats(@RequestParam(value="page",     defaultValue="0") String spage,
    		@RequestParam(value="per_page", defaultValue="0") String sper_page,@RequestParam(value = "estat", required=false) String estat, @RequestParam(value="tipusProducte", required=false) String tipusProducte, @RequestParam(value = "codiEmpressa", required=false) String codiEmpressa)	    		    		    	
    {    	
    	 List<Empressa> registresTotals = empressaRepository.findAll();
    	 List<InfoEmpressa> empressesList = new ArrayList<InfoEmpressa>();
    	 for (Empressa empressa : registresTotals) {
    		 InfoEmpressa empr = new InfoEmpressa();
    		 //NO FUNCIONA DE SOBTE , SENSE SENTIT, NO S'HA TOCAT RES
    		 List<EmpressaProducte> empresaProd = empressaProducteRepository.findAllStreamByEmpressa(empressa);
    		 empr.setCodi(empressa.getCodi());
    		 empr.setEstat(empressa.getEstat());
    		 //AFEGIR EL PRODUCTE DEL STREAM EMPRESA PROD I GRAVAR
    		 List<String> productes = new ArrayList<String>();
    	 		for (EmpressaProducte empre : empresaProd) {
    	 			productes.add(empre.getTipusProducte());
 			}
    	 		empr.setTipusProductes(productes);
    	 		empressesList.add(empr);
		}
    	 empressesList = filtrarAtributs(tipusProducte, estat, codiEmpressa, empressesList);
    	 
    	 
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
	    	 return new ResponseEntity<List<InfoEmpressa>> (empressesList.stream().collect(Collectors.toList()), HttpStatus.OK); 
	     else 
		     return new ResponseEntity<List<InfoEmpressa>> (empressesList.stream().skip(skip_reg).limit(per_page).collect(Collectors.toList()), HttpStatus.OK);
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/empresses_countFiltrat")
    public Long getRegisterCountFiltrat(@RequestParam(value = "estat", required=false) String estat, @RequestParam(value="tipusProducte", required=false) String tipusProducte, @RequestParam(value = "codiEmpressa", required=false) String codiEmpressa)	    		    		    	
    {    
    	
    	List<Empressa> registresTotals = empressaRepository.findAll();
   	 	List<InfoEmpressa> empressesList = new ArrayList<InfoEmpressa>();
   	 	for (Empressa empressa : registresTotals) {
   	 		InfoEmpressa empr = new InfoEmpressa();
   	 		List<EmpressaProducte> empresaProd = empressaProducteRepository.findAllStreamByEmpressa(empressa);
   	 		empr.setCodi(empressa.getCodi());
   	 		empr.setEstat(empressa.getEstat());
   	 		//AFEGIR EL PRODUCTE DEL STREAM EMPRESA PROD I GRAVAR
   	 		List<String> productes = new ArrayList<String>();
   	 		for (EmpressaProducte empre : empresaProd) {
   	 			productes.add(empre.getTipusProducte());
			}
   	 		empr.setTipusProductes(productes);
   	 		empressesList.add(empr);
		}
   	 	
   	 	empressesList = filtrarAtributs(tipusProducte, estat, codiEmpressa, empressesList);
    	return registresTotals.stream().count();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
}
package com.oapc.rest.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oapc.model.Empressa;
import com.oapc.model.EmpressaProducte;
import com.oapc.model.ErrorRest;
import com.oapc.model.InfoEmpressa;
import com.oapc.model.InfoRegistres;
import com.oapc.model.PDU;
import com.oapc.model.PepDTO;
import com.oapc.model.Periode;
import com.oapc.model.ProducteEmpressaPeriode;
import com.oapc.model.Register;
import com.oapc.model.RegisterDTO;
import com.oapc.model.RegistreNoComPerDTO;
import com.oapc.repo.EmpressaProducteRepository;
import com.oapc.repo.EmpressaRepository;
import com.oapc.repo.PduRepository;
import com.oapc.repo.PeriodeRepository;
import com.oapc.repo.ProducteEmpressaPeriodeRepository;
import com.oapc.repo.RegisterRepository;
import com.oapc.rest.v2.PduController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    
    @Autowired
    ProducteEmpressaPeriodeRepository	producteEmpressaPeriodeRepository;
   
    
    
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
    @GetMapping("/empresesByProd/{producte}")
    public List<String> getEmpressesByProducte(@PathVariable(value = "producte", required=false) String producte){
    	
    	List<Empressa> empressesList = empressaRepository.getEmpresesByProd(producte);
    	List<String> empList = new ArrayList<String>();
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
	@PostMapping("/newEmp")
	public void newEmpressa(@Valid @RequestBody InfoEmpressa newEmpressa) {
	
		Empressa existeix = empressaRepository.findByCodi(newEmpressa.getCodi());
		if (existeix == null) {
			existeix = new Empressa();
			existeix.setCodi(newEmpressa.getCodi());
			existeix.setEstat(newEmpressa.getEstat());
			//Es crea la nova empressa
			empressaRepository.save(existeix);
			
			for (String prod : newEmpressa.getTipusProductes()) {
				Empressa temporal = empressaRepository.findByCodi(newEmpressa.getCodi());
	        	EmpressaProducte temp = empressaProducteRepository.findAllListByProdAndEmpId(prod, temporal);
	        	if(temp == null) {
	        		EmpressaProducte updateEmpressaProd = new EmpressaProducte();
	        		temp = new EmpressaProducte();
	        		temp.setTipusProducte(prod);
	        		temp.setEmpressa(temporal);
	        		//Es crean les taules de empressaProducte
	        		updateEmpressaProd = empressaProducteRepository.save(temp);
	        	}
			}
			//TODO crear producteEmpressaPeriode pels periodes actuals i endavant que falten per la nova empressa i els seus productes
		
			Empressa temporal = empressaRepository.findByCodi(newEmpressa.getCodi());
			List<EmpressaProducte> listProdByEmp = empressaProducteRepository.findAllStreamByEmpressa(temporal);
			
			for (EmpressaProducte empressaProducte : listProdByEmp) {
				ProducteEmpressaPeriode toSave = new ProducteEmpressaPeriode();
				toSave.setNo_comercialitzacio(false);
				toSave.setPendent(false);
				toSave.setRegistrat(false);
				toSave.setTancat(true);
				toSave.setEmpressaProducte(empressaProducte);
				//empressaProducte.getTipusProducte();
				String typeProduct = pduController.getProductsType(empressaProducte.getTipusProducte());
				String typePeriod = new String();
				
				if(typeProduct.equals("PI")) {
					typePeriod = "S";
				}else if (typeProduct.equals("LL")) {
					typePeriod = "Q";
				}
				List<Periode> periodes = periodeRepository.getDatesByProductAndDate(typePeriod,getDataActualPerQuery());
				for (Periode periode : periodes) {
					toSave.setIdPeriode(periode);
					producteEmpressaPeriodeRepository.save(toSave);
				}
				
			}
		}
//	    l'empressa ja existeix
	}
    
    public Date getDataActualPerQuery() {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	Date dataActual = new Date();
    	
    	String dateConverting = formatter.format(dataActual);
    	LocalDate localDate = LocalDate.parse(dateConverting, formatter2);
    	return java.sql.Date.valueOf(localDate);
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
        EmpressaProducte updateEmpressaProd = new EmpressaProducte();
        List<EmpressaProducte> empresaProd = empressaProducteRepository.findAllStreamByEmpressa(emp);
        for (EmpressaProducte empressaProducte : empresaProd) {
			empressaProducteRepository.delete(empressaProducte);
		}
        
        for (String prod : empressa.getTipusProductes()) {
        	EmpressaProducte temp = empressaProducteRepository.findAllListByProdAndEmpId(prod, emp);
        	if(temp == null) {
        		temp = new EmpressaProducte();
        		temp.setTipusProducte(prod);
        		temp.setEmpressa(emp);
        		updateEmpressaProd = empressaProducteRepository.save(temp);
        	}
		}
        
        Empressa updatedEmpressa = empressaRepository.save(emp);
        return ResponseEntity.ok(updatedEmpressa);
    }
    
    @Transactional(readOnly = false)
    @PutMapping("/gestioPeriode")
    public ResponseEntity<ProducteEmpressaPeriode> updatePeriodeProdEmp(@Valid @RequestBody List<String> productes) {
    	List<ProducteEmpressaPeriode> registres = producteEmpressaPeriodeRepository.findAllListByProd(productes);
    	for (ProducteEmpressaPeriode producteEmpressaPeriode : registres) {
			producteEmpressaPeriode.setTancat(true);
			producteEmpressaPeriode.setNo_comercialitzacio(false);
			producteEmpressaPeriode.setPendent(false);
			producteEmpressaPeriode.setRegistrat(false);
			
			producteEmpressaPeriode = producteEmpressaPeriodeRepository.save(producteEmpressaPeriode);
		}
    	return null;
    }
    
    @Transactional(readOnly = false)
    @PutMapping("/gestioPep")
    public ResponseEntity<ProducteEmpressaPeriode> updatePep(@Valid @RequestBody PepDTO objPep) {
    	ProducteEmpressaPeriode pepToUpdate = producteEmpressaPeriodeRepository.findOne(objPep.getId());
    	if (pepToUpdate != null) {
    		pepToUpdate.setNo_comercialitzacio(objPep.getNoComercialitzacio());
        	pepToUpdate.setPendent(objPep.getPendent());
        	pepToUpdate.setRegistrat(objPep.getRegistrat());
        	pepToUpdate.setTancat(objPep.getTancat());
        	
        	pepToUpdate = producteEmpressaPeriodeRepository.save(pepToUpdate);
    	}
    	
    	return ResponseEntity.ok(pepToUpdate);
    }
    
    @Transactional(readOnly = false)
    @PutMapping("/regNoComPer")
    public ResponseEntity<Empressa> updateRegNoComPer(@Valid @RequestBody RegistreNoComPerDTO registre) {

//    	Empressa emp = empressaRepository.findByCodi(registre.getEmpresa());
//    	EmpressaProducte temporal = empressaProducteRepository.findAllListByProdAndEmpId(registre.getProducte(), emp);
    	Periode emp = periodeRepository.findOne(registre.getPeriode());
    	ProducteEmpressaPeriode test = producteEmpressaPeriodeRepository.findByPerProdAndCodiEmp(emp, registre.getProducte(), registre.getEmpresa());
    	test.setNo_comercialitzacio(true);
    	producteEmpressaPeriodeRepository.save(test);
    	return null;
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
    		 List<EmpressaProducte> empresaProd = empressaProducteRepository.findAllStreamByEmpressa(empressa);
    		 empr.setCodi(empressa.getCodi());
    		 empr.setEstat(empressa.getEstat());
    		 
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
    ///////////////////////////////////// PAGINATION PEP ///////////////////////////////////////////////
    
    @Transactional(readOnly = true)
    @GetMapping("/registresPEPFiltrat")
    public ResponseEntity<?> getRegistresPEPFiltratsPaginats(@RequestParam(value="page",     defaultValue="0") String spage,
    		@RequestParam(value="per_page", defaultValue="0") String sper_page,@RequestParam(value = "periode", required=false) String periode, 
    		@RequestParam(value="tipusProducte", required=false) String tipusProducte, @RequestParam(value="empresa", required=false) String empresa, 
    		@RequestParam(value="estat", required=false) String estat)	    		    		    	
    {    	    	 
//    	 Stream<Register> regi stresTotals = registreRepository.findAllStream();
    	 List<ProducteEmpressaPeriode> registresTotals = producteEmpressaPeriodeRepository.findAll();
    	 List<PepDTO> regis = new ArrayList<PepDTO>();
    	 registresTotals = filtrarAtributs(tipusProducte, periode, empresa, estat, registresTotals);
    	 
    	 
    	 Integer page      = Integer.parseInt(spage);
   	 	 Integer per_page  = Integer.parseInt(sper_page);
   	 	 
	     if (page == 0)
	    	 return new ResponseEntity<ErrorRest> (new ErrorRest("page not defined"), HttpStatus.BAD_REQUEST);
   	 	     	    	
	     if (per_page == 0)
	    	 return new ResponseEntity<ErrorRest> (new ErrorRest("per_page not defined"), HttpStatus.BAD_REQUEST);	     
	     //
//    	 Stream<Register> stream_cont = registreRepository.findAllStream();
    	 
//    	 Long    total_reg = stream_cont.count();
    	 Long    total_reg = registresTotals.stream().count();
    	 Long    page_max  = (total_reg / per_page) + 1;
    	 Integer skip_reg  = (page - 1) * per_page;    	 
	     	     	     	     
	     if (page > page_max)
	    	 return new ResponseEntity<ErrorRest > (new ErrorRest("page > page_max"), HttpStatus.BAD_REQUEST);
    	
    	 //
//    	 Stream<Register> stream_data = registreRepository.findAllStream();    	     	     	
	     
	     logger.info("page=" + page.toString() + ",page_max=" + page_max.toString() + ",per_page=" + per_page.toString() + ",total_reg=" +  total_reg.toString());	    
	     //logger.info(registresTotals.get(0).toString()); 
	     for (ProducteEmpressaPeriode register : registresTotals) {
 			
 			PepDTO test = new PepDTO();
 			test.setDataUltimRegistre(register.getDataUltimRegistre());
 			test.setEmpressaProducte(register.getEmpressaProducte().getTipusProducte());
 			test.setIdPeriode(register.getIdPeriode().getNumPeriode() + register.getIdPeriode().getTipusPeriode());
 			test.setId(register.getId());
 			test.setCodiEmpresa(register.getEmpressaProducte().getEmpressa().getCodi());
 			test.setNo_comercialitzacio(register.getNoComercialitzacio());
 			test.setPendent(register.getPendent());
 			test.setRegistrat(register.getRegistrat());
 			test.setTancat(register.getTancat());
 	        regis.add(test);
			}
	     if (page == 0)	    	
	    	 return new ResponseEntity<List<PepDTO>> (regis.stream().collect(Collectors.toList()), HttpStatus.OK); 
	     else 
		     return new ResponseEntity<List<PepDTO>> (regis.stream().skip(skip_reg).limit(per_page).collect(Collectors.toList()), HttpStatus.OK);
    }
//    
    @Transactional(readOnly = true)
    @GetMapping("/registresPEP_count")
    public Long getRegisterPEPCountFiltrat(@RequestParam(value = "periode", required=false) String periode, @RequestParam(value="tipusProducte", required=false) String tipusProducte, 
    		@RequestParam(value="empressa", required=false) String empressa, @RequestParam(value="estat", required=false) String estat)	    		    		    	
    {    
    	
    	 List<ProducteEmpressaPeriode> registresTotals = producteEmpressaPeriodeRepository.findAll();
    	 
    	 registresTotals = filtrarAtributs(tipusProducte, periode, empressa, estat, registresTotals);
    	
    	 return registresTotals.stream().count();
    }
    
    
    public List<ProducteEmpressaPeriode> filtrarAtributs(String tipusProducte, String periode, String empressa, String estat, List<ProducteEmpressaPeriode> registresTotals){
    	
    	
    	registresTotals = registresTotals.stream()
    			  .filter(x -> tipusProducte == null || x.getEmpressaProducte().getTipusProducte().equals(tipusProducte))
//	              .filter(x -> periode     == null || x.getIdPeriode().equals(periode.getId()))
	              .filter(x -> empressa  == null     || x.getEmpressaProducte().getEmpressa().getCodi().equals(empressa))
//	              .filter(x -> estat   == null     || x.get.equals(calibre))
	              .collect(Collectors.toList());
    	
    	if(estat != null) {
    		switch (Integer.valueOf(estat)) {
			case 1:
				registresTotals = registresTotals.stream()
									.filter(x -> estat == null || x.getPendent().equals(true))
									.collect(Collectors.toList());
				break;
			
			case 2:
				registresTotals = registresTotals.stream()
									.filter(x -> estat == null || x.getRegistrat().equals(true))
									.collect(Collectors.toList());
				break;

			case 3:
				registresTotals = registresTotals.stream()
									.filter(x -> estat == null || x.getNoComercialitzacio().equals(true))
									.collect(Collectors.toList());
				break;
			
			case 4:
				registresTotals = registresTotals.stream()
									.filter(x -> estat == null || x.getTancat().equals(true))
									.collect(Collectors.toList());
				break;
			default:
				break;
			}
    	}
    	
    	if (periode != null) {
    		char numPeriode = periode.charAt(0);//numero de periode
        	char tipusPeriode = periode.charAt(1);//tipus de periode
        	Periode periodeToSave = periodeRepository.findPeriodByNumType(Character.getNumericValue(numPeriode), String.valueOf(tipusPeriode));	
        	registresTotals = registresTotals.stream()
        			.filter(x -> periode   == null     || x.getIdPeriode().equals(periodeToSave))
  	              .collect(Collectors.toList());
    	}
    	
	 return registresTotals;
    }
}
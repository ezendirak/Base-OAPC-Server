package com.oapc.rest.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oapc.model.Empressa;
import com.oapc.model.ErrorRegister;
import com.oapc.model.ErrorRest;
import com.oapc.model.Periode;
import com.oapc.model.PeriodeDTO;
import com.oapc.model.Register;
import com.oapc.model.RegisterDTO;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
@RequestMapping("/api/v4")
public class RegisterController {

	private final Logger logger = LoggerFactory.getLogger(RegisterController.class);
	
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

//    private String[] taules = {"FAMILIA", "PRODUCTE", "SUBFAMILIA", "GRUP", "SUBGRUP", "COLORCARN", "QUALITAT", "CALIBRE"};
    
    
    
    @Transactional(readOnly = true)
    @GetMapping("/registres_page")
    public ResponseEntity<?> getAllRegistrePage(
    		@RequestParam(value="page",     defaultValue="0") String spage,
    		@RequestParam(value="per_page", defaultValue="0") String sper_page
    		)	    		    		    	
    {
   	 	 Integer page      = Integer.parseInt(spage);
   	 	 Integer per_page  = Integer.parseInt(sper_page);
   	 	 
	     if (page == 0)
	    	 return new ResponseEntity<ErrorRest> (new ErrorRest("page not defined"), HttpStatus.BAD_REQUEST);
   	 	     	    	
	     if (per_page == 0)
	    	 return new ResponseEntity<ErrorRest> (new ErrorRest("per_page not defined"), HttpStatus.BAD_REQUEST);	     
	     //
    	 Stream<Register> stream_cont = registreRepository.findAllStream();
    	 
    	 Long    total_reg = stream_cont.count();
    	 Long    page_max  = (total_reg / per_page) + 1;
    	 Integer skip_reg  = (page - 1) * per_page;    	 
	     	     	     	     
	     if (page > page_max)
	    	 return new ResponseEntity<ErrorRest> (new ErrorRest("page > page_max"), HttpStatus.BAD_REQUEST);
    	
    	 //
    	 Stream<Register> stream_data = registreRepository.findAllStream();    	     	     	
	     
	     logger.info("page=" + page.toString() + ",page_max=" + page_max.toString() + ",per_page=" + per_page.toString() + ",total_reg=" +  total_reg.toString());	    
	    	    	     
	     if (page == 0)	    	
	    	 return new ResponseEntity<List<Register>> (stream_data.collect(Collectors.toList()), HttpStatus.OK); 
	     else 
		     return new ResponseEntity<List<Register>> (stream_data.skip(skip_reg).limit(per_page).collect(Collectors.toList()), HttpStatus.OK);	    		 	    		 
    }    
        
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Transactional(readOnly = true)
    @GetMapping("/registres_count")
    public Long getRegisterCount()	    		    		    	
    {    	    	 
    	 Stream<Register> stream_cont = registreRepository.findAllStream();    	 	     
    	 return stream_cont.count();	     
    }

    
    @GetMapping("/registres/{id}")
    public ResponseEntity<RegisterDTO> getRegistreById(@PathVariable(value = "id") Long regisId) {
    	Register note = registreRepository.findOne(regisId);
        if(note == null) {
            return ResponseEntity.notFound().build();
        }
        RegisterDTO test = new RegisterDTO();
        test.setCalibre(note.getCalibre());
        test.setColorCarn(note.getColorCarn());
        test.setId(note.getId());
        test.setTipusProducte(note.getTipusProducte());
        test.setPeriode(note.getPeriode().getNumPeriode().toString());
        test.setPreuSortida(note.getPreuSortida());
        test.setQualitat(note.getQualitat());
        test.setQuantitatVenuda(note.getQuantitatVenuda());
        test.setVarietat(note.getVarietat());
        
        return ResponseEntity.ok().body(test);
    }
    
//    private String[] combos = {"COLORCARN", "VARIETAT", "QUALITAT", "CALIBRE"};
    @PostMapping("/registres")
    public Register createRegister(@Valid @RequestBody RegisterDTO registre) {
    	Periode peri = periodeRepository.findOne(Long.valueOf(registre.getPeriode()));
    	Empressa emp = empressaRepository.findOne(2L);
//    	Empressa emp2 = empressaRepository.findById(1L);
//    	long prova = registre.getPeriode();
//    	logger.info(String.valueOf(prova));
    	Register regi = new Register();
    	regi.setCalibre(registre.getCalibre());
    	regi.setColorCarn(registre.getColorCarn());
    	regi.setPreuSortida(registre.getPreuSortida());
    	regi.setVarietat(registre.getVarietat());
    	regi.setQualitat(registre.getQualitat());
    	regi.setQuantitatVenuda(registre.getQuantitatVenuda());
    	regi.setTipusProducte(registre.getTipusProducte());
    	regi.setPeriode(peri);
    	regi.setEmpressa(emp);
        return registreRepository.save(regi);
    }
    
    @PostMapping("/fromExcelRegistres")
    public Register createRegisterFromExcel(@Valid @RequestBody RegisterDTO registre, @RequestParam(value = "Familia", required=false) String familia) {
    	
    	if(!pduController.existeEn("PRODUCTE", registre.getTipusProducte())) {
    		//ERROR EN EL TIPUS DE PRODUCTE
    		logger.info("ERROR. El producte "+ registre.getTipusProducte() + " no està donat d'alta.");
//    		ErrorRegister errorRegistre = new ErrorRegister();
//    		errorRegistre.setTipusProducte(registre.getTipusProducte());
    		//funcio general per pasar de registre a Error
    	}else if (!pduController.existeEn("COLORCARN", registre.getColorCarn()) && familia == "1") {
    		//ERROR EN EL COLOR DE LA CARN
    		logger.info("ERROR. El color de carn "+ registre.getColorCarn() + " no està donat d'alta.");
    		
    	}else if (!pduController.existeEn("VARIETAT", registre.getVarietat()) && familia == "2") {
    		//ERROR EN LA VARIETAT
    		logger.info("ERROR. La varietat "+ registre.getVarietat() + " no està donat d'alta.");
    		
    	}else if (!pduController.existeEn("QUALITAT", registre.getQualitat())) {
    		//ERROR EN EL QUALITAT
    		logger.info("ERROR. La qualitat "+ registre.getQualitat() + " no està donada d'alta.");
    		
    	}else if (!pduController.existeEn("CALIBRE", registre.getCalibre())) {
    		//ERROR EN EL CALIBRE
    		logger.info("ERROR. El calibre "+ registre.getCalibre() + " no està donat d'alta.");
    		
    	}else {
    		Periode peri = periodeRepository.findOne(Long.valueOf(registre.getPeriode()));
        	Empressa emp = empressaRepository.findOne(2L);
//        	Empressa emp2 = empressaRepository.findById(1L);
//        	long prova = registre.getPeriode();
//        	logger.info(String.valueOf(prova));
        	Register regi = new Register();
        	regi.setCalibre(registre.getCalibre());
        	regi.setColorCarn(registre.getColorCarn());
        	regi.setPreuSortida(registre.getPreuSortida());
        	regi.setVarietat(registre.getVarietat());
        	regi.setQualitat(registre.getQualitat());
        	regi.setQuantitatVenuda(registre.getQuantitatVenuda());
        	regi.setTipusProducte(registre.getTipusProducte());
        	regi.setPeriode(peri);
        	regi.setEmpressa(emp);
        	return registreRepository.save(regi);
    	}
    	//QUE FEM EN AQUEST CAS
        return null;
    }
    
//    3
    @PostMapping("/downloadToExcel")
    public void crearExcelFromDataTable(@RequestBody List<RegisterDTO> frmData) throws IOException
    {
    	if (frmData != null) {
    		logger.info("NO ES NULL");
    		
    		String ruta = "C:/Users/pgarenas/Desktop/provaExcel.xls";
            File archivo = new File(ruta);
            BufferedWriter bw;
            if(archivo.exists()) {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write("El fichero de texto ya estaba creado.");
            } else {
            	
            	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            	DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            	Date dataActual = new Date();
            	
            	String dateConverting = formatter.format(dataActual);
            	LocalDate localDate = LocalDate.parse(dateConverting, formatter2);
            	
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write("Id\tPeriode\tTipus de producte\tVarietat\tColor de carn\tCalibre\tQualitat\tQuantitat Venuda (Kg)\tPreu de Sortida\tQuantitat per preu");
                bw.write("\n");
                for (RegisterDTO registerDTO : frmData) {
					bw.write(registerDTO.getId() + "\t" + registerDTO.getPeriode() + "\t" + registerDTO.getTipusProducte() + "\t" + registerDTO.getVarietat() + "\t" + registerDTO.getColorCarn() + "\t" + registerDTO.getCalibre() + "\t" + registerDTO.getQualitat() + "\t" + registerDTO.getQuantitatVenuda() + "\t" + registerDTO.getPreuSortida() + "\t" + (registerDTO.getQuantitatVenuda() * registerDTO.getPreuSortida()));
					bw.write("\n");
                }
                bw.write("\n");
                bw.write("Data de les dades: \t" + String.valueOf(java.sql.Date.valueOf(localDate)));
            }
            bw.close();
    	}else {
    		logger.info("ES FAKIN NULL");
    	}
    }
    
    @PutMapping("/registres")
    public ResponseEntity<Register> updateRegister(@Valid @RequestBody Register registerDetails) {
    	
    	Register registre = registreRepository.findOne(registerDetails.getId());
    	
        if(registre == null) {
            return ResponseEntity.notFound().build();
        }
        if (registerDetails.getPeriode()!=null) {
        	registre.setPeriode(registerDetails.getPeriode());
        }
        if (registerDetails.getTipusProducte()!=null) {
        	registre.setTipusProducte(registerDetails.getTipusProducte());
        }
        if (registerDetails.getColorCarn()!=null) {
        	registre.setColorCarn(registerDetails.getColorCarn());
        }
        if (registerDetails.getVarietat()!=null) {
        	registre.setVarietat(registerDetails.getVarietat());
        }
        if (registerDetails.getCalibre()!=null) {       
        	registre.setCalibre(registerDetails.getCalibre());
        }
        if (registerDetails.getQualitat()!=null) {
        	registre.setQualitat(registerDetails.getQualitat());
        }
        if (registerDetails.getPreuSortida()!=null) {
        	registre.setPreuSortida(registerDetails.getPreuSortida());
        }
        if (registerDetails.getQuantitatVenuda()!=null) {
        	registre.setQuantitatVenuda(registerDetails.getQuantitatVenuda());
        }
        
        Register updatedRegistre = registreRepository.save(registre);
//        Register updateRegistre = registreRepository.c
        return ResponseEntity.ok(updatedRegistre);
    }
    
    @DeleteMapping("/registres/{id}")
    public ResponseEntity<Register> deleteRegistre(@PathVariable(value = "id") Long registreId) {
    	Register registre = registreRepository.findOne(registreId);
        if(registre == null) {
            return ResponseEntity.notFound().build();
        }

        registreRepository.delete(registre);
        return ResponseEntity.ok().build();
    }
    
//    @Transactional(readOnly = true)
//    @GetMapping("/fromFiltro")
//    public List<Pdu> getAllColors2(@RequestParam(value = "color", required=false) String color, @RequestParam(value="diametre", required=false) Long diametre) {
//    	Stream<Pdu> producteStream = pduRepository.findAllStream();
//    	logger.info("Color: " + color + ", Diametre: "+ diametre);
//    	
//    	if (color != null) {
//    		producteStream = producteStream.filter(x -> x.getColor().equals(color));	
//    	}
//    	if(diametre != null){
//    		producteStream = producteStream.filter(x -> x.getDiametre().equals(diametre));
//    	}
//    	return producteStream.collect(Collectors.toList());
//    } 
    
    
    @GetMapping("/registres")
    public List<Register> getAllRegisters() {
    	return registreRepository.findAll();        
    }
    
    
    @Transactional(readOnly = true)
    @GetMapping("/filtro")
    public List<RegisterDTO> getRegistresFiltrats(@RequestParam(value = "colorCarn", required=false) String colorCarn, @RequestParam(value="tipusProducte", required=false) String tipusProducte, @RequestParam(value="qualitat", required=false) String qualitat, @RequestParam(value="calibre", required=false) String calibre, @RequestParam(value="varietat", required=false) String varietat)	    		    		    	
    {    	    	 
//    	 Stream<Register> registresTotals = registreRepository.findAllStream();
    	 List<Register> registresTotals = registreRepository.findAll();
    	 List<RegisterDTO> regis = new ArrayList<RegisterDTO>();
    	 if (registresTotals != null && (tipusProducte != null && !tipusProducte.isEmpty())) {
    		 registresTotals = registresTotals.stream().filter(x -> x.getTipusProducte().equals(tipusProducte)).collect(Collectors.toList());
    		 
    		
    		registresTotals = registresTotals.stream()
    				              .filter(x -> colorCarn == null || x.getColorCarn().equals(colorCarn))
    				              .filter(x -> qualitat  == null || x.getQualitat().equals(qualitat))
    				              .filter(x -> calibre   == null || x.getCalibre().equals(calibre))
    				              .filter(x -> varietat  == null || x.getVarietat().equals(varietat))
    				              .collect(Collectors.toList());
    		
    		
    		for (Register register : registresTotals) {
    			
    			RegisterDTO test = new RegisterDTO();
    	        test.setCalibre(register.getCalibre());
    	        test.setColorCarn(register.getColorCarn());
    	        test.setId(register.getId());
    	        test.setTipusProducte(register.getTipusProducte());
    	        test.setPeriode(register.getPeriode().getNumPeriode().toString());
    	        test.setPreuSortida(register.getPreuSortida());
    	        test.setQualitat(register.getQualitat());
    	        test.setQuantitatVenuda(register.getQuantitatVenuda());
    	        regis.add(test);
			}
    	 }
    	 return regis;     
    }
    
    
    @Transactional(readOnly = true)
    @GetMapping("/registresFiltrat")
    public ResponseEntity<?> getRegistresFiltratsPaginats(@RequestParam(value="page",     defaultValue="0") String spage,
    		@RequestParam(value="per_page", defaultValue="0") String sper_page,@RequestParam(value = "colorCarn", required=false) String colorCarn, @RequestParam(value="tipusProducte", required=false) String tipusProducte, 
    		@RequestParam(value="qualitat", required=false) String qualitat, @RequestParam(value="calibre", required=false) String calibre, @RequestParam(value="varietat", required=false) String varietat, 
    		@RequestParam(value="periode", required=false) String periode, @RequestParam(value="qVenuda", required=false) String qVenuda, @RequestParam(value="qVenuda2", required=false) String qVenuda2,
    		@RequestParam(value="pSortida", required=false) String pSortida, @RequestParam(value="pSortida2", required=false) String pSortida2, @RequestParam(value="eInformant", required=false) String eInformant)	    		    		    	
    {    	    	 
//    	 Stream<Register> registresTotals = registreRepository.findAllStream();
    	 List<Register> registresTotals = registreRepository.findAll();
    	 List<RegisterDTO> regis = new ArrayList<RegisterDTO>();
    	 registresTotals = filtrarAtributs(tipusProducte, colorCarn, qualitat, calibre, varietat, periode, registresTotals, qVenuda, qVenuda2, pSortida, pSortida2, eInformant);
    	 
    	 
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
	     for (Register register : registresTotals) {
 			
 			RegisterDTO test = new RegisterDTO();
 	        test.setCalibre(register.getCalibre());
 	        test.setColorCarn(register.getColorCarn());
 	        test.setId(register.getId());
 	        test.setTipusProducte(register.getTipusProducte());
 	        test.setPeriode(register.getPeriode().getNumPeriode().toString());
 	        test.setPreuSortida(register.getPreuSortida());
 	        test.setQualitat(register.getQualitat());
 	        test.setQuantitatVenuda(register.getQuantitatVenuda());
 	        test.setVarietat(register.getVarietat());
 	        regis.add(test);
			}
	     if (page == 0)	    	
	    	 return new ResponseEntity<List<RegisterDTO>> (regis.stream().collect(Collectors.toList()), HttpStatus.OK); 
	     else 
		     return new ResponseEntity<List<RegisterDTO>> (regis.stream().skip(skip_reg).limit(per_page).collect(Collectors.toList()), HttpStatus.OK);
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/registres_countFiltrat")
    public Long getRegisterCountFiltrat(@RequestParam(value = "colorCarn", required=false) String colorCarn, @RequestParam(value="tipusProducte", required=false) String tipusProducte, 
    		@RequestParam(value="qualitat", required=false) String qualitat, @RequestParam(value="calibre", required=false) String calibre, @RequestParam(value="varietat", required=false) String varietat, 
    		@RequestParam(value="periode", required=false) String periode, @RequestParam(value="qVenuda", required=false) String qVenuda, @RequestParam(value="qVenuda2", required=false) String qVenuda2, 
    		@RequestParam(value="pSortida", required=false) String pSortida, @RequestParam(value="pSortida2", required=false) String pSortida2,@RequestParam(value="eInformant", required=false) String eInformant)	    		    		    	
    {    
    	
    	 List<Register> registresTotals = registreRepository.findAll();
    	 
    	 registresTotals = filtrarAtributs(tipusProducte, colorCarn, qualitat, calibre, varietat, periode, registresTotals, qVenuda, qVenuda2, pSortida, pSortida2, eInformant);
    	
    	 return registresTotals.stream().count();
    }
    
    
    public List<Register> filtrarAtributs(String tipusProducte, String colorCarn, String qualitat, String calibre, String varietat, String periode, List<Register> registresTotals, String qVenuda, String qVenuda2, String pSortida, String pSortida2, String eInformant){
    	
    	Empressa empressa = empressaRepository.findByCodi(eInformant);
    	registresTotals = registresTotals.stream()
    			  .filter(x -> tipusProducte == null 	|| 	x.getTipusProducte().equals(tipusProducte))
	              .filter(x -> colorCarn     == null 	|| 	x.getColorCarn().equals(colorCarn))
	              .filter(x -> qualitat  == null     	|| 	x.getQualitat().equals(qualitat))
	              .filter(x -> calibre   == null     	|| 	x.getCalibre().equals(calibre))
	              .filter(x -> varietat  == null     	|| 	x.getVarietat().equals(varietat))
	              .filter(x -> qVenuda == null		 	|| 	x.getQuantitatVenuda() > Long.valueOf(qVenuda))
	              .filter(x -> qVenuda2 == null		 	|| 	x.getQuantitatVenuda() < Long.valueOf(qVenuda2))
	              .filter(x -> pSortida == null		 	|| 	x.getPreuSortida() > Long.valueOf(pSortida))
	              .filter(x -> pSortida2 == null		|| 	x.getPreuSortida() < Long.valueOf(pSortida2))
	              .filter(x -> eInformant == null		||	x.getEmpressa().equals(empressa))
	              .collect(Collectors.toList());
    	
    	if (periode != null) {
    		char numPeriode = periode.charAt(0);//numero de periode
        	char tipusPeriode = periode.charAt(1);//tipus de periode
        	Periode periodeToSave = periodeRepository.findPeriodByNumType(Character.getNumericValue(numPeriode), String.valueOf(tipusPeriode));	
        	registresTotals = registresTotals.stream()
        			.filter(x -> periode   == null     || x.getPeriode().equals(periodeToSave))
  	              .collect(Collectors.toList());
    	}
    	
	 return registresTotals;
    }
    
    
    @Transactional(readOnly = true)
    @GetMapping("/periodesTotals")
    public List<PeriodeDTO> getPeriodesTotals() throws ParseException{
    	
    	List<Periode> streamPeriode = periodeRepository.findAllList();
    	List<PeriodeDTO> periodesList = new ArrayList<PeriodeDTO>();
    	
    	PeriodeDTO temp2 = new PeriodeDTO();

    	temp2.setNumPeriode(0);
		temp2.setTipusPeriode("Tots");
		periodesList.add(temp2);
		
    	for (Periode peri : streamPeriode) {
			PeriodeDTO temp = new PeriodeDTO();
			temp.setAny(peri.getAny());
			temp.setData_inici(peri.getDataInici());
			temp.setDataFi(peri.getDataFi());
			temp.setDuracio(peri.getDuracio());
			temp.setId(peri.getId());
			temp.setNumPeriode(peri.getNumPeriode());
			temp.setTipusPeriode(peri.getTipusPeriode());
			periodesList.add(temp);
		}
    	
    	return periodesList;
    	
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/periodesDisponibles")
    public List<PeriodeDTO> getPeriodesDisponibles() throws ParseException{
    	
//    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//    	DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    	Date dataActual = new Date();
//    	
//    	String dateConverting = formatter.format(dataActual);
////    	Date formatedDate = Date.from(Instant.parse(dateConverting));2018-05-07
//    	LocalDate localDate = LocalDate.parse(dateConverting, formatter2);
////    	LocalDate localDate2 = LocalDate.parse("2018-05-08", formatter2);
    	Stream<Periode> streamPeriode = periodeRepository.getDatesDisponibles();
    	List<PeriodeDTO> periodesList = new ArrayList<PeriodeDTO>();
    	
    	for (Periode peri : streamPeriode.collect(Collectors.toList())) {
			PeriodeDTO temp = new PeriodeDTO();
			temp.setAny(peri.getAny());
			temp.setData_inici(peri.getDataInici());
			temp.setDataFi(peri.getDataFi());
			temp.setDuracio(peri.getDuracio());
			temp.setId(peri.getId());
			temp.setNumPeriode(peri.getNumPeriode());
			temp.setTipusPeriode(peri.getTipusPeriode());
			periodesList.add(temp);
		}
//    	return streamPeriode.collect(Collectors.toList());
    	return periodesList;
    }
    
    
    //PERIODES PEL FILTRE DE REGISTRE
    @Transactional(readOnly = true)
    @GetMapping("/periodesByProd/{subGrup}")
    public List<PeriodeDTO> getPeriodesByProd(@PathVariable(value = "subGrup", required=false) String subGrup){
    	

    	Stream<Periode> streamPeriode = periodeRepository.getDatesDisponibles();
    	if (subGrup.equals("PI")) {
    		streamPeriode = streamPeriode.filter(x -> x.getTipusPeriode().equals("S"));
    	}else if (subGrup.equals("LL")) {
    		streamPeriode = streamPeriode.filter(x -> x.getTipusPeriode().equals("Q"));
    	}
    	
    	List<PeriodeDTO> periodesList = new ArrayList<PeriodeDTO>();
    	
    	for (Periode peri : streamPeriode.collect(Collectors.toList())) {
			PeriodeDTO temp = new PeriodeDTO();
			temp.setAny(peri.getAny());
			temp.setData_inici(peri.getDataInici());
			temp.setDataFi(peri.getDataFi());
			temp.setDuracio(peri.getDuracio());
			temp.setId(peri.getId());
			temp.setNumPeriode(peri.getNumPeriode());
			temp.setTipusPeriode(peri.getTipusPeriode());
			periodesList.add(temp);
		}
//    	return streamPeriode.collect(Collectors.toList());
    	return periodesList;
    }
    
//    @PathVariable(value = "subGrup", required=false) String subGrup
    
}
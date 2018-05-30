package com.oapc.rest.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oapc.model.EmpressaProducte;
import com.oapc.model.ErrorRest;
import com.oapc.model.PDU;
import com.oapc.model.Periode;
import com.oapc.model.PeriodeDTO;
import com.oapc.model.ProducteEmpressaPeriode;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api/v5")
public class GestioPeriodeController {

	private String[] combos = {"COLORCARN", "VARIETAT", "QUALITAT", "CALIBRE"};
	
	private final Logger logger = LoggerFactory.getLogger(GestioPeriodeController.class);
	
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
    ProducteEmpressaPeriodeRepository producteEmpressaPeriodeRepository;

    @Autowired
    GestioEmpressaController	gestioEmpressaController;
   
    @Transactional(readOnly = false)
	@PostMapping("/newPeriodes")
    @PreAuthorize("hasRole('USER')")
	public void newCalendar(@Valid @RequestBody List<PeriodeDTO> periodesNous) {
    	List<PeriodeDTO> periodesErrors = new ArrayList<PeriodeDTO>();
		for (PeriodeDTO periode : periodesNous) {
			if(!periode.getNumPeriode().equals(null) && !periode.getTipusPeriode().equals(null)) {
				Periode existeix = periodeRepository.findPeriodByNumType(periode.getNumPeriode(), periode.getTipusPeriode());
				if(existeix == null) {
					Periode periodeFinal = new Periode();
					periodeFinal.setAny(periode.getAny());
					periodeFinal.setData_inici(periode.getDataInici());
					periodeFinal.setDataFi(periode.getDataFi());
					periodeFinal.setNumPeriode(periode.getNumPeriode());
					periodeFinal.setTipusPeriode(periode.getTipusPeriode());
					periodeRepository.save(periodeFinal);
					
					////////////////////////////////////////////////////
					
					List<EmpressaProducte> listEmpProd = empressaProducteRepository.findAll();
					Periode existeix2 = periodeRepository.findPeriodByNumType(periode.getNumPeriode(), periode.getTipusPeriode());
					
					for (EmpressaProducte empressaProducte : listEmpProd) {
						Stream<PDU> producte = pduRepository.getProducteByDades("PRODUCTE", empressaProducte.getTipusProducte());
						PDU prodObj = producte.collect(Collectors.toList()).get(0);
						String tipusProd = prodObj.getDatos().substring(32, 34).trim();
						//registre.getDatos().substring(32, 34).trim()
						if ((tipusProd.equals("PI") && existeix2.getTipusPeriode().equals("S")) || (tipusProd.equals("LL") && existeix2.getTipusPeriode().equals("Q"))) {
							ProducteEmpressaPeriode toSave = new ProducteEmpressaPeriode();
							toSave.setNo_comercialitzacio(false);
							toSave.setPendent(false);
							toSave.setRegistrat(false);
							toSave.setTancat(true);
							toSave.setEmpressaProducte(empressaProducte);
							toSave.setIdPeriode(existeix2);
							
							producteEmpressaPeriodeRepository.save(toSave);
						}
					}
				}else {
					//El nou periode introduït te el mateix numero i tipus de periode que un periode existent
					periodesErrors.add(periode);
					//TODO enrregistrar els errors  (log o per pantalla)
				}
			}
		}
		logger.info("Registres totals: "+ periodesNous.size() + ". Errors: (periodes repetits) " + periodesErrors.size());
		
	}
    
    
    public List<Periode> filtrarAtributs(String periode, Date dataInici, Date dataFi, List<Periode> registresTotals) throws ParseException{

    	registresTotals = registresTotals.stream()
    			  .filter(x -> periode == null || x.getNumPeriode().equals(Character.getNumericValue(periode.charAt(0))))
    			  .filter(x -> periode == null || x.getTipusPeriode().equals(String.valueOf(periode.charAt(1))))
    			  .filter(x -> dataInici == null || x.getDataInici().after(dataInici) || x.getDataFi().equals(dataInici))
    			  .filter(x -> dataFi == null || x.getDataFi().before(dataFi) || x.getDataFi().equals(dataFi))
	              .collect(Collectors.toList());
    	
	 return registresTotals;
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////FORMAT///////////////////////////////////////////////////////
    
    public void convertDate(Long data) {
    	Instant instant = Instant.ofEpochMilli(data);
//    	ZonedDateTime z = instant.atZone(ZoneId.of("Australia/Sydney"));
    	// format it
    	logger.info(String.valueOf(data));
    	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm dd/MM/yyyy");
    	System.out.println(fmt.format(instant));
    	logger.info(String.valueOf(instant));
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////PAGINATION////////////////////////////////////////////////////
    
    @Transactional(readOnly = true)
    @GetMapping("/periodesFiltrat")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getRegistresFiltratsPaginats(@RequestParam(value="page",     defaultValue="0") String spage,
    		@RequestParam(value="per_page", defaultValue="0") String sper_page,@RequestParam(value="periode", required=false) String periode,
    		@RequestParam(value="dataInici", required=false) Date dataInici, @RequestParam(value="dataFi", required=false) Date dataFi) throws ParseException	    		    		    	
    {    	    	 
   	
    	 List<Periode> registresTotals = periodeRepository.findAllList();
    	 registresTotals = filtrarAtributs(periode, dataInici, dataFi, registresTotals);
    	 
    	 
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
    	
	     List<PeriodeDTO> periodesList = new ArrayList<PeriodeDTO>();
	    	
	    for (Periode peri : registresTotals) {
			PeriodeDTO temp = new PeriodeDTO();
			temp.setAny(peri.getAny());
//			convertDate(peri.getDataInici());
//			Date cr = new Date(peri.getDataInici());
			temp.setData_inici(peri.getDataInici());
			temp.setDataFi(peri.getDataFi());
			temp.setDuracio(peri.getDuracio());
			temp.setId(peri.getId());
			temp.setNumPeriode(peri.getNumPeriode());
			temp.setTipusPeriode(peri.getTipusPeriode());
			periodesList.add(temp);
		}	     	
	     
	     logger.info("page=" + page.toString() + ",page_max=" + page_max.toString() + ",per_page=" + per_page.toString() + ",total_reg=" +  total_reg.toString());	    
	   
	     if (page == 0)	    	
	    	 return new ResponseEntity<List<PeriodeDTO>> (periodesList.stream().collect(Collectors.toList()), HttpStatus.OK); 
	     else 
		     return new ResponseEntity<List<PeriodeDTO>> (periodesList.stream().skip(skip_reg).limit(per_page).collect(Collectors.toList()), HttpStatus.OK);
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/periodes_countFiltrat")
    @PreAuthorize("hasRole('USER')")
    public Long getRegisterCountFiltrat(@RequestParam(value="periode", required=false) String periode,
    		@RequestParam(value="dataInici", required=false) Date dataInici, @RequestParam(value="dataFi", required=false) Date dataFi) throws ParseException	    		    		    	
    {    
    	
    	 List<Periode> registresTotals = periodeRepository.findAll();
    	 
    	 registresTotals = filtrarAtributs(periode, dataInici, dataFi, registresTotals);
    	
    	 return registresTotals.stream().count();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
}
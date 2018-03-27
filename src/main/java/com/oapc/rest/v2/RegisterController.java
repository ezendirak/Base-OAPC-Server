package com.oapc.rest.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oapc.model.ErrorRest;
import com.oapc.model.Register;
import com.oapc.repo.RegisterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v4")
public class RegisterController {

	private final Logger logger = LoggerFactory.getLogger(RegisterController.class);
	
    @Autowired
    RegisterRepository registreRepository;

//    private String[] taules = {"FAMILIA", "PRODUCTE", "SUBFAMILIA", "GRUP", "SUBGRUP", "COLORCARN", "QUALITAT", "CALIBRE"};
    
    
        
//    private static boolean isContain(String source, String subItem){
////        String pattern = "\\b"+subItem+"\\b";
//        String sourceUnprocess = source;
//        source = source.replaceAll(subItem, "");
//        if(source.equals(sourceUnprocess)) {
//        	return false;
//        }else {
//        	return true;
//        }
//   }
    
    
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
    public ResponseEntity<Register> getRegistreById(@PathVariable(value = "id") Long noteId) {
    	Register note = registreRepository.findOne(noteId);
        if(note == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(note);
    }

    @PostMapping("/registres")
    public Register createRegister(@Valid @RequestBody Register note) {
        return registreRepository.save(note);
    }

    @PutMapping("/registres/{id}")
    public ResponseEntity<Register> updateRegister(@PathVariable(value = "id") Long noteId,
                                           @Valid @RequestBody Register registerDetails) {
    	
    	Register registre = registreRepository.findOne(noteId);
        if(registre == null) {
            return ResponseEntity.notFound().build();
        }
        
//        registre.setPeriode(registerDetails.getPeriode());
//        registre.setTipusProducte(registerDetails.getTipusProducte());
//        
//        if (registerDetails.getColorCarn()!=null) {
//        	registre.setColorCarn(registerDetails.getColorCarn());
//        }
//        if (registerDetails.getVarietat()!=null) {
//        	registre.setVarietat(registerDetails.getVarietat());
//        }
//                
//        registre.setCalibre(registerDetails.getCalibre());
//        registre.setQualitat(registerDetails.getQualitat());
//        registre.setPreuSortida(registerDetails.getPreuSortida());
//        registre.setQuantitatVenuda(registerDetails.getQuantitatVenuda());

        
        Register updatedRegistre = registreRepository.save(registre);
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
    public List<Register> getRegistresFiltrats(@RequestParam(value = "colorCarn", required=false) String colorCarn, @RequestParam(value="tipusProducte", required=false) String tipusProducte, @RequestParam(value="qualitat", required=false) String qualitat, @RequestParam(value="calibre", required=false) String calibre, @RequestParam(value="varietat", required=false) String varietat)	    		    		    	
    {    	    	 
//    	 Stream<Register> registresTotals = registreRepository.findAllStream();
    	 List<Register> registresTotals = registreRepository.findAll();
    	 if (registresTotals != null && (tipusProducte != null && !tipusProducte.isEmpty())) {
    		 registresTotals = registresTotals.stream().filter(x -> x.getTipusProducte().equals(tipusProducte)).collect(Collectors.toList());
    		 
    		
    		registresTotals = registresTotals.stream()
    				              .filter(x -> colorCarn == null || x.getColorCarn().equals(colorCarn))
    				              .filter(x -> qualitat  == null || x.getQualitat().equals(qualitat))
    				              .filter(x -> calibre   == null || x.getCalibre().equals(calibre))
    				              .filter(x -> varietat  == null || x.getVarietat().equals(varietat))
    				              .collect(Collectors.toList());
    	   
    		 
//    		 if (qualitat != null) { 
//    			 
//    				 registresTotals = registresTotals.stream().filter(x -> qualitat == null ||x.getQualitat().equals(qualitat)).collect(Collectors.toList());
//    			}
//    		 
//    		 if (calibre != null) {
//    				 registresTotals = registresTotals.stream().filter(x -> calibre == null || x.getCalibre().equals(calibre)).collect(Collectors.toList()); 
//    			}
//    		 
//    		 if (varietat != null) {
//    				 registresTotals = registresTotals.stream().filter(x -> varietat == null || x.getVarietat().equals(varietat)).collect(Collectors.toList());	
//    			}
    	 }
    	 return registresTotals;     
//    	 return registresTotals.stream().filter(x -> x.getTipusProducte().equals(tipusProducte)).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/registresFiltrat")
    public ResponseEntity<?> getRegistresFiltratsPaginats(@RequestParam(value="page",     defaultValue="0") String spage,
    		@RequestParam(value="per_page", defaultValue="0") String sper_page,@RequestParam(value = "colorCarn", required=false) String colorCarn, @RequestParam(value="tipusProducte", required=false) String tipusProducte, @RequestParam(value="qualitat", required=false) String qualitat, @RequestParam(value="calibre", required=false) String calibre, @RequestParam(value="varietat", required=false) String varietat)	    		    		    	
    {    	    	 
//    	 Stream<Register> registresTotals = registreRepository.findAllStream();
    	 List<Register> registresTotals = registreRepository.findAll();
    	 
    	 registresTotals = filtrarAtributs(tipusProducte, colorCarn, qualitat, calibre, varietat, registresTotals);
    	 
    	 
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
	        	     
	     if (page == 0)	    	
	    	 return new ResponseEntity<List<Register>> (registresTotals.stream().collect(Collectors.toList()), HttpStatus.OK); 
	     else 
		     return new ResponseEntity<List<Register>> (registresTotals.stream().skip(skip_reg).limit(per_page).collect(Collectors.toList()), HttpStatus.OK);
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/registres_countFiltrat")
    public Long getRegisterCountFiltrat(@RequestParam(value = "colorCarn", required=false) String colorCarn, @RequestParam(value="tipusProducte", required=false) String tipusProducte, @RequestParam(value="qualitat", required=false) String qualitat, @RequestParam(value="calibre", required=false) String calibre, @RequestParam(value="varietat", required=false) String varietat)	    		    		    	
    {    
    	
    	 List<Register> registresTotals = registreRepository.findAll();
    	 
    	 registresTotals = filtrarAtributs(tipusProducte, colorCarn, qualitat, calibre, varietat, registresTotals);
    	
    	 return registresTotals.stream().count();
    }
    
    public List<Register> filtrarAtributs(String tipusProducte, String colorCarn, String qualitat, String calibre, String varietat, List<Register> registresTotals){
    	
    	registresTotals = registresTotals.stream()
    			  .filter(x -> tipusProducte == null || x.getTipusProducte().equals(tipusProducte))
	              .filter(x -> colorCarn     == null || x.getColorCarn().equals(colorCarn))
	              .filter(x -> qualitat  == null     || x.getQualitat().equals(qualitat))
	              .filter(x -> calibre   == null     || x.getCalibre().equals(calibre))
	              .filter(x -> varietat  == null     || x.getVarietat().equals(varietat))
	              .collect(Collectors.toList());
    	
	 return registresTotals;
    }
}
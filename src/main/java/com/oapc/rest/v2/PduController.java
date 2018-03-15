package com.oapc.rest.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oapc.model.ErrorRest;
import com.oapc.model.PDU;
import com.oapc.repo.PduRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v3")
public class PduController {

	private final Logger logger = LoggerFactory.getLogger(PduController.class);
	
    @Autowired
    PduRepository pduRepository;

    private String[] taules = {"FAMILIA", "PRODUCTE", "SUBFAMILIA", "GRUP", "SUBGRUP", "COLORCARN", "QUALITAT", "CALIBRE"};
    private String taulaTrobada;
    private String keyTrobada;
    
    @Transactional(readOnly = true)
    @GetMapping("/pdu/{datos}")
    public List<PDU> getAllColors(@PathVariable(value = "datos") String datos) {
    	logger.info("PDU: " + datos);
    	for (String pduTaula : taules) {
    		if (isContain(datos,pduTaula)) {
//    			String regex = "\\s*\\b"+pduTaula+"\\b\\s*";
    			datos = datos.replaceAll(pduTaula, "");
    			setTaulaTrobada(pduTaula);
    			setKeyTrobada(datos);
    			logger.info("Dins del for, taula: " + pduTaula);
    			logger.info("Dins del for, temp: " + datos);
    		}
		}
    	logger.info("PDU: " + datos);
    	//DATOS/TEMP ara nomes tenim la key(clave)
//    	Stream<PDU> pduStream = pduRepository.findAllStream();
    	Stream<PDU> pduStream = pduRepository.getDades(getTaulaTrobada(), getKeyTrobada());
//    	pduStream = pduStream.filter(x -> x.getTabla().equals(getTaulaTrobada())).filter(x -> x.getClave().contains(getKeyTrobada()));
        return pduStream.collect(Collectors.toList());                     
    }
    
    private static boolean isContain(String source, String subItem){
//        String pattern = "\\b"+subItem+"\\b";
        String sourceUnprocess = source;
        source = source.replaceAll(subItem, "");
        if(source.equals(sourceUnprocess)) {
        	return false;
        }else {
        	return true;
        }
   }
    
    @Transactional(readOnly = true)
    @GetMapping("/pdu_page")
    public ResponseEntity<?> getAllPduPage(
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
    	 Stream<PDU> stream_cont = pduRepository.findAllStream();
    	 
    	 Long    total_reg = stream_cont.count();
    	 Long    page_max  = (total_reg / per_page) + 1;
    	 Integer skip_reg  = (page - 1) * per_page;    	 
	     	     	     	     
	     if (page > page_max)
	    	 return new ResponseEntity<ErrorRest> (new ErrorRest("page > page_max"), HttpStatus.BAD_REQUEST);
    	
    	 //
    	 Stream<PDU> stream_data = pduRepository.findAllStream();    	     	     	
	     
	     logger.info("page=" + page.toString() + ",page_max=" + page_max.toString() + ",per_page=" + per_page.toString() + ",total_reg=" +  total_reg.toString());	    
	    	    	     
	     if (page == 0)	    	
	    	 return new ResponseEntity<List<PDU>> (stream_data.collect(Collectors.toList()), HttpStatus.OK); 
	     else 
		     return new ResponseEntity<List<PDU>> (stream_data.skip(skip_reg).limit(per_page).collect(Collectors.toList()), HttpStatus.OK);	    		 	    		 
    }    
        
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Transactional(readOnly = true)
    @GetMapping("/pdu_count")
    public Long getPduCount()	    		    		    	
    {    	    	 
    	 Stream<PDU> stream_cont = pduRepository.findAllStream();    	 	     
    	 return stream_cont.count();	     
    }

	public String getTaulaTrobada() {
		return taulaTrobada;
	}

	public void setTaulaTrobada(String taulaTrobada) {
		this.taulaTrobada = taulaTrobada;
	}

	public String getKeyTrobada() {
		return keyTrobada;
	}

	public void setKeyTrobada(String keyTrobada) {
		this.keyTrobada = keyTrobada;
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
    public List<PDU> getAllProducts() {
    	return pduRepository.findAll();        
    }

//    @GetMapping("/fromFiltro/{color}")
//    public ResponseEntity<Note> getNoteById(@PathVariable(value = "color") String color) {
//    	
//        Note note = colorRepository.findOne(color);
//        if(note == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok().body(note);
//    }

//    @Transactional(readOnly = true)
//    @GetMapping("/pdu/{datos}")
//    public List<PDU> getNameProducts {
//    	Stream<PDU> pduStream = pduRepository.getDades("PRODUCTE", null);
//    	return pduStream.collect(Collectors.toList());                     
//    }
    
    @Transactional(readOnly = true)
    @GetMapping("/pdu/productes")
    public List<String> getProductsName(){
    	String vacio = "";
    	Stream<PDU> pduStream = pduRepository.getDades("PRODUCTE", vacio);
    	List<String> productes = new ArrayList<String>();
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
			productes.add(registre.getDatos().substring(0, 25).trim());
		}
    	return productes;
    }
    
    

}
package com.oapc.rest.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oapc.model.AtributsCombo;
import com.oapc.model.ErrorRest;
import com.oapc.model.InfoRegistres;
import com.oapc.model.PDU;
import com.oapc.repo.PduRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v3")
public class PduController {

	private final Logger logger = LoggerFactory.getLogger(PduController.class);
	
    @Autowired
    PduRepository pduRepository;

    private String[] taules = {"FAMILIA", "PRODUCTE", "SUBFAMILIA", "GRUP", "SUBGRUP", "COLORCARN", "QUALITAT", "CALIBRE"};
    private String[] combos = {"COLORCARN", "VARIETAT", "QUALITAT", "CALIBRE"};
    private String taulaTrobada;
    private String keyTrobada;
    
    @Transactional(readOnly = true)
    @GetMapping("/pdu/datos/{datos}")
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
    	productes.add("");
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
//			productes.add(registre.getDatos().substring(0, 25).trim());
			productes.add(registre.getClave());
		}
    	return productes;
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/pdu/PROPDU")
    public List<InfoRegistres> getProductsName2(){
    	String vacio = "";
    	Stream<PDU> pduStream = pduRepository.getDades("PRODUCTE", vacio);
    	InfoRegistres producte = new InfoRegistres();
    	producte.setClau("");
    	producte.setNom("Tots");
    	List<InfoRegistres> productes = new ArrayList<InfoRegistres>();
    	productes.add(producte);
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
//			productes.add(registre.getDatos().substring(0, 25).trim());
//			productes.add(registre.getClave());
    		producte = new InfoRegistres();
    		producte.setClau(registre.getClave());
    		producte.setNom(registre.getDatos().substring(0, 25).trim());
    		producte.setSubGrup(registre.getDatos().substring(32, 34).trim());
    		productes.add(producte);
		}
    	return productes;
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/pdu/productesModal")
    public List<InfoRegistres> getProductsNameForModal(){
    	String vacio = "";
    	Stream<PDU> pduStream = pduRepository.getDades("PRODUCTE", vacio);
    	
    	List<InfoRegistres> productes = new ArrayList<InfoRegistres>();
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
//			productes.add(registre.getDatos().substring(0, 25).trim());
//			productes.add(registre.getClave());
    		InfoRegistres producte = new InfoRegistres();
    		producte.setClau(registre.getClave());
    		producte.setNom(registre.getDatos().substring(0, 25).trim());
    		producte.setSubGrup(registre.getDatos().substring(32, 34).trim());
    		productes.add(producte);
		}
    	return productes;
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/pdu/combos/{tipusProducte}")
    public AtributsCombo getCombos(@PathVariable(value = "tipusProducte") String tipusProducte){
    	
    	AtributsCombo atributsCombo = new AtributsCombo(); 
    	Stream<PDU> product = pduRepository.getDades("PRODUCTE", tipusProducte.replaceAll("\"",""));
    	//String producteKey = product.collect(Collectors.toList()).get(0).getClave();
    	List<String> atributs = new ArrayList<String>();
    	for (PDU atribut : product.collect(Collectors.toList())) {
			atributs.add(atribut.getClave());
		}
    	
    	
    	
    	if (atributs.size() > 1) {
    		List<String> producteKey = new ArrayList<String>();
    		for (String producte : atributs) {
				producteKey.add(producte);
			}
    		atributsCombo = combosSenseProducte(producteKey, atributsCombo);
    	}else {
    		String producteKey= atributs.get(0);
    		atributsCombo = combosAmbProducte(producteKey, atributsCombo);
    	}
    	
    	
    	return atributsCombo;
    }
    
    public AtributsCombo combosSenseProducte(List<String> productesKey, AtributsCombo atributsCombo){
    	
    	for (String atribut : combos) {
    		
    		switch (atribut) {
    		case "COLORCARN":
    			List<InfoRegistres> colorsCarns = new ArrayList<InfoRegistres>();
    			InfoRegistres colorCarn = new InfoRegistres();
    			colorCarn.setClau("");
				colorCarn.setNom("-");
				colorsCarns.add(colorCarn);
    			for (String producteKey : productesKey) {
    				for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
						if (!colorsCarns.contains(regis.getClave().substring(producteKey.length()))) {
//							colorsCarns.add(regis.getClave().substring(producteKey.length()));
							colorCarn = new InfoRegistres();
							colorCarn.setClau(regis.getClave().substring(producteKey.length()));
							colorCarn.setNom(regis.getDatos());
							colorsCarns.add(colorCarn);
						}
					}
				}
    			
    			atributsCombo.setColorsCarn(colorsCarns);
    			break;
    		case "VARIETAT":
    			List<InfoRegistres> varietats = new ArrayList<InfoRegistres>();
    			InfoRegistres varietat = new InfoRegistres();
    			varietat.setClau("");
    			varietat.setNom("-");
    			varietats.add(varietat);
    			for (String producteKey : productesKey) {
	    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
	    				if (!varietats.contains("VA"+regis.getClave().substring(producteKey.length()))) {
//	    					varietats.add("VA"+regis.getClave().substring(producteKey.length()));
	    					varietat = new InfoRegistres();
	        				varietat.setClau("VA"+regis.getClave().substring(producteKey.length()));
	        				varietat.setNom(regis.getDatos());
	        				varietats.add(varietat);
	    				}
					}
    			}
    				atributsCombo.setVarietats(varietats);
    			break;
    		case "QUALITAT":
    			List<InfoRegistres> qualitats = new ArrayList<InfoRegistres>();
    			InfoRegistres qualitat = new InfoRegistres();
    			qualitat.setClau("");
    			qualitat.setNom("-");
    			qualitats.add(qualitat);
    			for (String producteKey : productesKey) {
	    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
	    				if (!qualitats.contains("QU"+regis.getClave().substring(producteKey.length()))) {
//	    					qualitats.add("QU"+regis.getClave().substring(producteKey.length()));
	    					qualitat = new InfoRegistres();
	        				qualitat.setClau("QU"+regis.getClave().substring(producteKey.length()));
	        				qualitat.setNom(regis.getDatos());
	        				qualitats.add(qualitat);
	    				}
					}
    			}
    				atributsCombo.setQualitats(qualitats);
    			break;
    		case "CALIBRE":
    			List<InfoRegistres> calibres = new ArrayList<InfoRegistres>();
    			InfoRegistres calibre = new InfoRegistres();
    			calibre.setClau("");
    			calibre.setNom("-");
    			calibres.add(calibre);
    			for (String producteKey : productesKey) {
	    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
	    				if (!calibres.contains("CA"+regis.getClave().substring(producteKey.length()))) {
//	    					calibres.add("CA"+regis.getClave().substring(producteKey.length()));
	    					calibre = new InfoRegistres();
	            			calibre.setClau("CA"+regis.getClave().substring(producteKey.length()));
	            			calibre.setNom(regis.getDatos());
	            			calibres.add(calibre);
	    				}
					}
    			}
    				atributsCombo.setCalibres(calibres);
    			break;
    		}
		}
    	return atributsCombo;
    }
    
    public AtributsCombo combosAmbProducte(String producteKey, AtributsCombo atributsCombo) {
    	
    	for (String atribut : combos) {
    		
    		switch (atribut) {
    		case "COLORCARN":
    			List<InfoRegistres> colorsCarns = new ArrayList<InfoRegistres>();
    			InfoRegistres colorCarn = new InfoRegistres();
    			colorCarn.setClau("");
				colorCarn.setNom("-");
				colorsCarns.add(colorCarn);
    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
    				
//					colorsCarns.add(regis.getClave().substring(producteKey.length()));
    				colorCarn = new InfoRegistres();
					colorCarn.setClau(regis.getClave().substring(producteKey.length()));
					colorCarn.setNom(regis.getDatos());
					colorsCarns.add(colorCarn);
				}
    				atributsCombo.setColorsCarn(colorsCarns);
    			break;
    		case "VARIETAT":
    			List<InfoRegistres> varietats = new ArrayList<InfoRegistres>();
    			InfoRegistres varietat = new InfoRegistres();
    			varietat.setClau("");
    			varietat.setNom("-");
    			varietats.add(varietat);
    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
//    				varietats.add("VA"+regis.getClave().substring(producteKey.length()));
    				varietat = new InfoRegistres();
    				varietat.setClau("VA"+regis.getClave().substring(producteKey.length()));
    				varietat.setNom(regis.getDatos());
    				varietats.add(varietat);
				}
    				atributsCombo.setVarietats(varietats);
    			break;
    		case "QUALITAT":
    			List<InfoRegistres> qualitats = new ArrayList<InfoRegistres>();
    			InfoRegistres qualitat = new InfoRegistres();
    			qualitat.setClau("");
    			qualitat.setNom("-");
    			qualitats.add(qualitat);
    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
//    				qualitats.add("QU"+regis.getClave().substring(producteKey.length()));
    				qualitat = new InfoRegistres();
    				qualitat.setClau("QU"+regis.getClave().substring(producteKey.length()));
    				qualitat.setNom(regis.getDatos());
    				qualitats.add(qualitat);
				}
    				atributsCombo.setQualitats(qualitats);
    			break;
    		case "CALIBRE":
    			List<InfoRegistres> calibres = new ArrayList<InfoRegistres>();
    			InfoRegistres calibre = new InfoRegistres();
    			calibre.setClau("");
    			calibre.setNom("-");
    			calibres.add(calibre);
    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
//    				calibres.add("CA"+regis.getClave().substring(producteKey.length()));
    				calibre = new InfoRegistres();
        			calibre.setClau("CA"+regis.getClave().substring(producteKey.length()));
        			calibre.setNom(regis.getDatos());
        			calibres.add(calibre);
				}
    				atributsCombo.setCalibres(calibres);
    			break;
    		}
		}
    	return atributsCombo;
    }
//    @Transactional(readOnly = true)
//    @GetMapping("/pdu/combos/{tipusProducte}")
//    public AtributsCombo getCombos(@PathVariable(value = "tipusProducte") String tipusProducte){
//    	
//    	AtributsCombo atributsCombo = new AtributsCombo(); 
//    	Stream<PDU> product = pduRepository.getProducteByDades("PRODUCTE", tipusProducte.replaceAll("\"",""));
//    	//String producteKey = product.collect(Collectors.toList()).get(0).getClave();
//    	List<String> atributs = new ArrayList<String>();
//    	for (PDU atribut : product.collect(Collectors.toList())) {
//			atributs.add(atribut.getClave());
//		}
//    	String producteKey = atributs.get(0);
//    	for (String atribut : combos) {
//    		
//    		switch (atribut) {
//    		case "COLORCARN":
////    			List<String> colorsCarns = new ArrayList<String>();
//    			Map<String, String> colorsCarns = new HashMap<String, String>();
//    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
//					colorsCarns.put(regis.getClave(), regis.getDatos());
//				}
//    				atributsCombo.setColorsCarn(colorsCarns);
//    			break;
//    		case "VARIETAT":
//    			Map<String, String> varietats = new HashMap<String, String>();
//    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
//    				varietats.put(regis.getClave(), regis.getDatos());
//				}
//    				atributsCombo.setVarietats(varietats);
//    			break;
//    		case "QUALITAT":
//    			Map<String, String> qualitats = new HashMap<String, String>();
//    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
//    				qualitats.put(regis.getClave(), regis.getDatos());
//				}
//    				atributsCombo.setQualitats(qualitats);
//    			break;
//    		case "CALIBRE":
//    			Map<String, String> calibres = new HashMap<String, String>();
//    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
//    				calibres.put(regis.getClave(), regis.getDatos());
//				}
//    				atributsCombo.setCalibres(calibres);
//    			break;
//    		}
//		}
//    	return atributsCombo;
//    }
    
    @Transactional(readOnly = true)
    @GetMapping("/pduCombos")
    public Map<String, AtributsCombo> getAllCombosProducts() {
    	List<String> ProductsNames = getProductsName();
    	Map<String, AtributsCombo> combosByProduct = new HashMap<String, AtributsCombo>();
    	for (String product : ProductsNames) {
			combosByProduct.put(product, getCombos(product));
		}
    	return combosByProduct;
    }
    
//    @Transactional(readOnly = true)
//    @GetMapping("/pdu/colorcarn/{tipusProducte}")
//    public List<String> getColorCarn(@PathVariable(value = "tipusProducte") String tipusProducte){
//    	
//    	Stream<PDU> product = pduRepository.getProducteByDades("PRODUCTE", tipusProducte);
//    	String producteKey = product.collect(Collectors.toList()).get(0).getClave();
//    	
//    	Stream<PDU> pduStream = pduRepository.getDades("COLORCARN", producteKey);
//    	List<String> productes = new ArrayList<String>();
//    	for (PDU registre : pduStream.collect(Collectors.toList())) {
//			productes.add(registre.getDatos());
//		}
//    	return productes;
//    }
//    
//    @Transactional(readOnly = true)
//    @GetMapping("/pdu/varietat/{tipusProducte}")
//    public List<String> getVarietat(@PathVariable(value = "tipusProducte") String tipusProducte){
//    	
//    	Stream<PDU> product = pduRepository.getProducteByDades("PRODUCTE", tipusProducte);
//    	String producteKey = product.collect(Collectors.toList()).get(0).getClave();
//    	
//    	Stream<PDU> pduStream = pduRepository.getDades("VARIETAT", producteKey);
//    	List<String> productes = new ArrayList<String>();
//    	for (PDU registre : pduStream.collect(Collectors.toList())) {
//			productes.add(registre.getDatos());
//		}
//    	return productes;
//    }
//    
//    @Transactional(readOnly = true)
//    @GetMapping("/pdu/qualitat/{tipusProducte}")
//    public List<String> getQualitat(@PathVariable(value = "tipusProducte") String tipusProducte){
//    	
//    	Stream<PDU> product = pduRepository.getProducteByDades("PRODUCTE", tipusProducte);
//    	String producteKey = product.collect(Collectors.toList()).get(0).getClave();
//    	
//    	Stream<PDU> pduStream = pduRepository.getDades("QUALITAT", producteKey);
//    	List<String> productes = new ArrayList<String>();
//    	for (PDU registre : pduStream.collect(Collectors.toList())) {
//			productes.add(registre.getDatos());
//		}
//    	return productes;
//    }
//    
//    @Transactional(readOnly = true)
//    @GetMapping("/pdu/calibre/{tipusProducte}")
//    public List<String> getCalibre(@PathVariable(value = "tipusProducte") String tipusProducte){
//    	
//    	Stream<PDU> product = pduRepository.getProducteByDades("PRODUCTE", tipusProducte);
//    	String producteKey = product.collect(Collectors.toList()).get(0).getClave();
//    	
//    	Stream<PDU> pduStream = pduRepository.getDades("CALIBRE", producteKey);
//    	List<String> productes = new ArrayList<String>();
//    	for (PDU registre : pduStream.collect(Collectors.toList())) {
//			productes.add(registre.getDatos());
//		}
//    	return productes;
//    }
//    @Transactional(readOnly = true)
//    @GetMapping("/pdu/prova")
//    public List<List<String>> getProva(){
//    	
//    	List<List<String>> productes = new ArrayList<List<String>>();
//    	List<String> test = new ArrayList<String>();
//    	List<String> test2 = new ArrayList<String>();
//    	test.add("siuuu");
//    	test.add("skrrr");
//    	test2.add("segon lloc");
//    	test2.add("nonono pesad");
//    	productes.add(test);
//    	productes.add(test2);
//    	
//    	return productes;
//    }
    
    

}
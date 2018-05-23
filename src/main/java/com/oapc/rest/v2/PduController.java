package com.oapc.rest.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oapc.model.AtributsCombo;
import com.oapc.model.ErrorRest;
import com.oapc.model.InfoRegistres;
import com.oapc.model.NewProdDTO;
import com.oapc.model.PDU;
import com.oapc.repo.PduRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    
    //enviem un atribut i ens retorna tots els valors posibles (atribut => colorcarn o varietat o qualitat o calibre...)
    @Transactional(readOnly = true)
    @GetMapping("/pdu/datos/{datos}")
    @PreAuthorize("hasRole('GESTOR')")
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
    	
    	Stream<PDU> pduStream = pduRepository.getDades(getTaulaTrobada(), getKeyTrobada());
    	return pduStream.collect(Collectors.toList());                     
    }
    
//    public List<PDU> getAllValues(@PathVariable(value = "clau") String clau, @PathVariable(value = "atribut") String atribut) {
//    	return pduRepository.
//    }
    
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
    @PreAuthorize("hasRole('GESTOR')")
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
    @PreAuthorize("hasRole('GESTOR')")
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


    //Dona la clau, no el name
    @Transactional(readOnly = true)
    @GetMapping("/pdu/productes")
    @PreAuthorize("hasRole('GESTOR')")
    public List<String> getProductsName(){
    	String vacio = "";
    	Stream<PDU> pduStream = pduRepository.getDades("PRODUCTE", vacio);
    	List<String> productes = new ArrayList<String>();
    	productes.add("");
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
    		
			productes.add(registre.getClave());
		}
    	return productes;
    }
    
    @Transactional(readOnly = true)
    
    public List<String> getProductsTrueNames(){
    	String vacio = "";
    	Stream<PDU> pduStream = pduRepository.getDades("PRODUCTE", vacio);
    	List<String> productes = new ArrayList<String>();
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
			productes.add(registre.getDatos().substring(0, 25).trim());
			
		}
    	return productes;
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/pdu/PROPDU")
    @PreAuthorize("hasRole('GESTOR')")
    public List<InfoRegistres> getProductsName2(){
    	String vacio = "";
    	Stream<PDU> pduStream = pduRepository.getDades("PRODUCTE", vacio);
    	InfoRegistres producte = new InfoRegistres();
    	producte.setClau("");
    	producte.setNom("Tots");
    	List<InfoRegistres> productes = new ArrayList<InfoRegistres>();
    	productes.add(producte);
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
    @GetMapping("/pdu/FAMPDU")
    @PreAuthorize("hasRole('GESTOR')")
    public List<InfoRegistres> getFamilyName(){
    	String vacio = "";
    	Stream<PDU> pduStream = pduRepository.getDades("SUBGRUP", vacio);
    	InfoRegistres producte = new InfoRegistres();
    	producte.setClau("");
    	producte.setNom("Totes");
    	List<InfoRegistres> productes = new ArrayList<InfoRegistres>();
    	productes.add(producte);
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
    		
    		producte = new InfoRegistres();
    		producte.setClau(String.valueOf(registre.getId()));
    		producte.setNom(registre.getDatos());
    		producte.setSubGrup(registre.getClave());
    		productes.add(producte);
		}
    	return productes;
    }
    
    
    public String getProductsType(String producte){
    	String tipus;
    	Stream<PDU> pduStream = pduRepository.getProducteByDades("PRODUCTE", producte);
    	
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
    		
    		tipus = registre.getDatos().substring(32, 34).trim();
    		return tipus;
		}
    	return null;
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/pdu/productesModal")
    @PreAuthorize("hasRole('GESTOR')")
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
    		producte.setId(registre.getId());
    		productes.add(producte);
		}
    	return productes;
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/productesModalByType/{subGrup}")
    @PreAuthorize("hasRole('GESTOR')")
    public List<InfoRegistres> getProductsByType(@PathVariable(value = "subGrup", required=false) String subGrup){
    	String vacio = "";
    	Stream<PDU> pduStream = pduRepository.getDades("PRODUCTE", vacio);
    	
    	List<InfoRegistres> productes = new ArrayList<InfoRegistres>();
    	for (PDU registre : pduStream.collect(Collectors.toList())) {
//			productes.add(registre.getDatos().substring(0, 25).trim());
//			productes.add(registre.getClave());
    		if ((subGrup.equals("S") && registre.getDatos().substring(32, 34).trim().equals("PI")) ||
    			(subGrup.equals("Q") && registre.getDatos().substring(32, 34).trim().equals("LL"))	) {
    			InfoRegistres producte = new InfoRegistres();
        		producte.setClau(registre.getClave());
        		producte.setNom(registre.getDatos().substring(0, 25).trim());
        		producte.setSubGrup(registre.getDatos().substring(32, 34).trim());
        		producte.setId(registre.getId());
        		productes.add(producte);
    		}
    		
		}
    	return productes;
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/pdu/combos/{tipusProducte}")
    @PreAuthorize("hasRole('GESTOR')")
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
    
    @Transactional(readOnly = true)
    public AtributsCombo getCombosName(String tipusProducte){
    	
    	AtributsCombo atributsCombo = new AtributsCombo(); 
    	Stream<PDU> product = pduRepository.getProducteByDades("PRODUCTE", tipusProducte);
    	//String producteKey = product.collect(Collectors.toList()).get(0).getClave();
    	List<String> atributs = new ArrayList<String>();
    	for (PDU atribut : product.collect(Collectors.toList())) {
			atributs.add(atribut.getClave());
		}
    	
    	if (atributs.size() > 2) {
    		List<String> producteKey = new ArrayList<String>();
    		for (String producte : atributs) {
				producteKey.add(producte);
			}
    		atributsCombo = combosSenseProducte(producteKey, atributsCombo);
    	}else {
    		String producteKey= atributs.get(0);
    		atributsCombo = combosAmbProducteModalToAdd(producteKey, atributsCombo);
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
    
    @Transactional(readOnly = true)
    @GetMapping("/pduCombos")
    @PreAuthorize("hasRole('GESTOR')")
    public Map<String, AtributsCombo> getAllCombosProducts() {
    	List<String> ProductsNames = getProductsName();
    	Map<String, AtributsCombo> combosByProduct = new HashMap<String, AtributsCombo>();
    	for (String product : ProductsNames) {
			combosByProduct.put(product, getCombos(product));
		}
    	return combosByProduct;
    }
    
    @Transactional(readOnly = true)
    @GetMapping("/pduNamedCombos")
    @PreAuthorize("hasRole('GESTOR')")
    public Map<String, AtributsCombo> getAllCombosNamesProducts() {
    	List<String> ProductsNames = getProductsTrueNames();
    	Map<String, AtributsCombo> combosByProduct = new HashMap<String, AtributsCombo>();
    	for (String product : ProductsNames) {
			combosByProduct.put(product, getCombosName(product));
		}
    	return combosByProduct;
    }
    
    ////////////////////////////////////////////////////////MODAL////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
   
    @Transactional(readOnly = true)
    @GetMapping("/pdu/ModalCombosToAdd/{tipusProducte}")
    @PreAuthorize("hasRole('GESTOR')")
    public AtributsCombo getCombosModalToAdd(@PathVariable(value = "tipusProducte") String tipusProducte){
    	
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
    		atributsCombo = combosSenseProducteModalToAdd(producteKey, atributsCombo);
    	}else {
    		String producteKey= atributs.get(0);
    		atributsCombo = combosAmbProducteModalToAdd(producteKey, atributsCombo);
    	}
    	
    	
    	return atributsCombo;
    }
    
    public AtributsCombo combosSenseProducteModalToAdd(List<String> productesKey, AtributsCombo atributsCombo){
    	
    	for (String atribut : combos) {
    		
    		switch (atribut) {
    		case "COLORCARN":
    			List<InfoRegistres> colorsCarns = new ArrayList<InfoRegistres>();
    			for (String producteKey : productesKey) {
    				for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
						if (!colorsCarns.contains(regis.getClave().substring(producteKey.length()))) {
//							colorsCarns.add(regis.getClave().substring(producteKey.length()));
							InfoRegistres colorCarn = new InfoRegistres();
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
    			for (String producteKey : productesKey) {
	    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
	    				if (!varietats.contains("VA"+regis.getClave().substring(producteKey.length()))) {
//	    					varietats.add("VA"+regis.getClave().substring(producteKey.length()));
	    					InfoRegistres varietat = new InfoRegistres();
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
    			for (String producteKey : productesKey) {
	    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
	    				if (!qualitats.contains("QU"+regis.getClave().substring(producteKey.length()))) {
//	    					qualitats.add("QU"+regis.getClave().substring(producteKey.length()));
	    					InfoRegistres qualitat = new InfoRegistres();
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
    			for (String producteKey : productesKey) {
	    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
	    				if (!calibres.contains("CA"+regis.getClave().substring(producteKey.length()))) {
//	    					calibres.add("CA"+regis.getClave().substring(producteKey.length()));
	    					InfoRegistres calibre = new InfoRegistres();
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
    
public AtributsCombo combosAmbProducteModalToAdd(String producteKey, AtributsCombo atributsCombo) {
    	
    	for (String atribut : combos) {
    		
    		switch (atribut) {
    		case "COLORCARN":
    			List<InfoRegistres> colorsCarns = new ArrayList<InfoRegistres>();
    			
    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
    				
//					colorsCarns.add(regis.getClave().substring(producteKey.length()));
    				InfoRegistres colorCarn = new InfoRegistres();
					colorCarn.setClau(regis.getClave().substring(producteKey.length()));
					colorCarn.setNom(regis.getDatos());
					colorsCarns.add(colorCarn);
				}
    				atributsCombo.setColorsCarn(colorsCarns);
    			break;
    		case "VARIETAT":
    			List<InfoRegistres> varietats = new ArrayList<InfoRegistres>();
    			
    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
//    				varietats.add("VA"+regis.getClave().substring(producteKey.length()));
    				InfoRegistres varietat = new InfoRegistres();
    				varietat.setClau("VA"+regis.getClave().substring(producteKey.length()));
    				varietat.setNom(regis.getDatos());
    				varietats.add(varietat);
				}
    				atributsCombo.setVarietats(varietats);
    			break;
    		case "QUALITAT":
    			List<InfoRegistres> qualitats = new ArrayList<InfoRegistres>();
    			
    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
//    				qualitats.add("QU"+regis.getClave().substring(producteKey.length()));
    				InfoRegistres qualitat = new InfoRegistres();
    				qualitat.setClau("QU"+regis.getClave().substring(producteKey.length()));
    				qualitat.setNom(regis.getDatos());
    				qualitats.add(qualitat);
				}
    				atributsCombo.setQualitats(qualitats);
    			break;
    		case "CALIBRE":
    			List<InfoRegistres> calibres = new ArrayList<InfoRegistres>();
    			
    			for (PDU regis : pduRepository.getDades(atribut, producteKey).collect(Collectors.toList())) {
//    				calibres.add("CA"+regis.getClave().substring(producteKey.length()));
    				InfoRegistres calibre = new InfoRegistres();
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



	@Transactional(readOnly = false)
	@PostMapping("/newAtribut")
	@PreAuthorize("hasRole('GESTOR')")
	public PDU newAtribut(@Valid @RequestBody NewProdDTO atributByProd) {
	
		PDU atribut = pduRepository.findOne(atributByProd.getIdProd());
		PDU newAtribut = new PDU();
		if (atribut != null) {
			
			if (pduRepository.getIfOldProduct(atribut.getClave(), atributByProd.getValor()).count() == 0) {
				String keyProd = atribut.getClave();
				
				newAtribut.setCont(1);
				newAtribut.setDatos(atributByProd.getValor());
				newAtribut.setTabla(atributByProd.getTaula());
				if (atributByProd.getTaula().equals("COLORCARN")) {
					newAtribut.setClave(keyProd+atributByProd.getValor().toUpperCase().charAt(0)+(atributByProd.getValor().toUpperCase().charAt(2)));
				}else {
					newAtribut.setClave(keyProd+(pduRepository.getDades(atributByProd.getTaula(), keyProd).count()+1));
				}
			}else {
				//El Producte ja conté un atribut amb aquest valor
				logger.info("El Producte ja conté un atribut amb aquest valor");
				
			}
		}
	    return pduRepository.save(newAtribut);
	}
	
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@Transactional(readOnly = true)
    public boolean existeEn(String valor, String atribut) {
    	
    	Stream<PDU> pduStream = pduRepository.getProducteByDades(valor, atribut);
    	
    	if (pduStream.collect(Collectors.toList()).size() > 0) {
    		return true;
    	}  else {
    		return false;
    	}
    }
	

    

}
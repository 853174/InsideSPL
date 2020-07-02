package com.onekin.insideSpl.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onekin.insideSpl.businessLogic.CMapBLInterface;
import com.onekin.insideSpl.businessLogic.ProductBLInterface;
import com.onekin.insideSpl.domain.VariantModel;
import com.onekin.insideSpl.domain.cmap.CMap;
import com.onekin.insideSpl.domain.cmap.Concept;

@Controller
@RequestMapping(value = "/cmaps")
public class CMapController {
	
	@Autowired
	private CMapBLInterface cmapBL;
	
	@Autowired
	private ProductBLInterface productBL;
	
	@RequestMapping(value="")
	public String cmaps(Model model) {
		
		if(MainController.session().getAttribute("selectedSplId") == null)
			return "redirect:/";
		
		String splId = (String) MainController.session().getAttribute("selectedSplId");
		
		List<CMap> cmaps = cmapBL.getCMapsOfSpl(splId);
		
		model.addAttribute("cmaps",cmaps);
		
		Map<String,String> cmapInfo = new HashMap<>();
		
		for(CMap cmap : cmaps) {
			cmapInfo.put(cmap.getId() + "-nodeArray",cmapBL.getNodeString(cmap));
			cmapInfo.put(cmap.getId() + "-linkArray",cmapBL.getLinkString(cmap));
		}
		
		model.addAttribute("cmapInfo", cmapInfo);
		
		return "cmapView/index";
	}
	
	@RequestMapping(value="{id}")
	public String cmap(Model model,@PathVariable(name = "id",required = true) String id) {
		
		if(MainController.session().getAttribute("selectedSplId") == null)
			return "redirect:/";
		
		CMap cmap = cmapBL.getCMapById(id);
		
		if(cmap == null)
			return "redirect:/cmaps";
		
		model.addAttribute("cmap",cmap);
		model.addAttribute("nodeArray",cmapBL.getNodeString(cmap));
		model.addAttribute("linkArray",cmapBL.getLinkString(cmap));
		model.addAttribute("resourceArray",cmapBL.getResourceString(cmap));
		model.addAttribute("featureConnArray",cmapBL.getRelatedFeaturesString(cmap));
		
		return "cmapView/cmap";
	}
	
	@RequestMapping(value="{id}/products")
	public String cmapProducts(Model model,@PathVariable(name = "id",required = true) String id) {
		
		if(MainController.session().getAttribute("selectedSplId") == null)
			return "redirect:/";
		
		String splId = (String) MainController.session().getAttribute("selectedSplId");
		
		CMap cmap = cmapBL.getCMapById(id);
		
		if(cmap == null)
			return "redirect:/cmaps";
		
		List<VariantModel> vms = productBL.getAllProductsOfSpl(splId);
		
		model.addAttribute("cmap",cmap);
		
		vms = productBL.orderByConceptCoverage(vms,cmap);
		
		model.addAttribute("products",vms);
		
		Map<String,Integer> productInfo = new HashMap<>();
		
		for(VariantModel product : vms) {
			// Get product's concept coverage
			List<Concept> coveredConcepts = productBL.getConceptCoverage(product,cmap); 
			productInfo.put(product.getId() + "-hasConcepts",coveredConcepts.size());
		}
		
		model.addAttribute("productInfo",productInfo);
		
		return "cmapView/products";
	}

}

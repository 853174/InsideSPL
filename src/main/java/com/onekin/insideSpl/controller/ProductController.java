package com.onekin.insideSpl.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onekin.insideSpl.businessLogic.ProductBLInterface;
import com.onekin.insideSpl.domain.VariantComponent;
import com.onekin.insideSpl.domain.VariantFeature;
import com.onekin.insideSpl.domain.VariantModel;
import com.onekin.insideSpl.utils.Triplet;

@Controller
@RequestMapping(value = "/products")
public class ProductController {

	@Autowired
	private ProductBLInterface productBL;

	@RequestMapping(value = "")
	public String index(Model model) {
		
		if(MainController.session().getAttribute("selectedSplId") == null)
			return "redirect:/";
		
		String splId = (String) MainController.session().getAttribute("selectedSplId");
		
		List<VariantModel> products = productBL.orderBySelectedFeaturesSize(productBL.getAllProductsOfSpl(splId));		
		
		model.addAttribute("products", products);
		model.addAttribute("splId",splId);
		
		Map<String,Integer> productInfo = new HashMap<>();
		
		for(VariantModel p : products) {
			int smf = 0;
			int sof = 0;
			int total = 0;
			
			for(VariantComponent vc : p.getVariants()) {
				if(vc instanceof VariantFeature) {
					total++;
					if(vc.isSelected()) {
						VariantFeature vf = (VariantFeature) vc;
						if(vf.getFeature().getType().contentEquals("MANDATORY"))
							smf++;
						else
							sof++;
					}
				}
			}
			
			productInfo.put(p.getId() + "-smf",smf);
			productInfo.put(p.getId() + "-sof",sof);
			productInfo.put(p.getId() + "-total",total);
		}
		
		model.addAttribute("productInfo", productInfo);
		
		return "productView/index";
	}
	
	@RequestMapping(value ="/{id}")
	public String product(Model model, @PathVariable(name = "id",required = true) String id) {
		
		if(MainController.session().getAttribute("selectedSplId") == null)
			return "redirect:/";
		
		String splId = (String) MainController.session().getAttribute("selectedSplId");
		
		VariantModel product = productBL.getProductById(id);
		
		if(product == null) {
			return "redirect:/";
		}
		
		Triplet<String, Integer, Integer> productInfo = productBL.getProductInfoById(id);
		String configurationJson = productBL.getProductConfigurationJsonById(id);
		
		model.addAttribute("splId",splId);
		model.addAttribute("product",product);
		model.addAttribute("data",productInfo.getFst());
		model.addAttribute("productSize",productInfo.getSnd());
		model.addAttribute("totalFeatures",productInfo.getTrd());
		model.addAttribute("configData",configurationJson);
		
		return "productView/product";
	}
	
	@RequestMapping(value ="/compare/{id1}/{id2}")
	public String compare(Model model, @PathVariable(name = "id1",required = true) String id1, @PathVariable(name = "id2",required = true) String id2) {
		
		if(MainController.session().getAttribute("selectedSplId") == null)
			return "redirect:/";
		
		VariantModel product1 = productBL.getProductById(id1);
		VariantModel product2 = productBL.getProductById(id2);
		
		if(product1 == null || product2 == null) {
			return "redirect:/";
		}
		
		Map<String, String> info = new HashMap<String, String>();
		
		VariantModel[] plist = {product1,product2};
		
		for(VariantModel p : plist ) {
			int smf = 0;
			int sof = 0;
			int tf = 0;
			
			for(VariantComponent vc : p.getVariants()) {
				if(vc instanceof VariantFeature) {
					if(vc.isSelected()) {
						VariantFeature vf = (VariantFeature) vc;
						if(vf.getFeature().getType().contentEquals("MANDATORY")) {
							smf++;
						}else {
							sof++;
						}
					}
					
					tf++;
				}
			}

			info.put(p.getId() + "_smf",Integer.toString(smf));
			info.put(p.getId() + "_sof",Integer.toString(sof));
			info.put(p.getId() + "_tf",Integer.toString(tf));
		}
		
		String comparationJson = productBL.compareProducts(product1,product2);
		
		model.addAttribute("prod1", product1);
		model.addAttribute("prod1data",productBL.getProductConfigurationJsonById(id1));
		
		model.addAttribute("prod2", product2);
		model.addAttribute("prod2data",productBL.getProductConfigurationJsonById(id2));
		
		model.addAttribute("info",info);
		model.addAttribute("similarityData", comparationJson);
		
		return "productView/compare";
	}

}

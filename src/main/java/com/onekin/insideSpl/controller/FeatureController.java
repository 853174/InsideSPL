package com.onekin.insideSpl.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onekin.insideSpl.businessLogic.CMapBLInterface;
import com.onekin.insideSpl.businessLogic.FeatureBLInterface;
import com.onekin.insideSpl.domain.Attribute;
import com.onekin.insideSpl.domain.Feature;
import com.onekin.insideSpl.domain.FeatureModel;
import com.onekin.insideSpl.domain.VariationPoint;
import com.onekin.insideSpl.domain.cmap.LinkElement;
import com.onekin.insideSpl.domain.cmap.Resource;
import com.onekin.insideSpl.utils.Pair;

@Controller
@RequestMapping(value = "/features")
public class FeatureController {
	
	@Autowired
	private FeatureBLInterface featureBL;
	
	@Autowired
	private CMapBLInterface cmapBL;
	
	
	@RequestMapping(value="")
	public String showFeatureModels(Model model) {
		
		if(MainController.session().getAttribute("selectedSplId") == null)
			return "redirect:/";
		
		String splId = (String) MainController.session().getAttribute("selectedSplId");
		
		List<FeatureModel> featureModels = featureBL.getAllFeatureModelsOfSpl(splId);
		
		System.out.println(featureModels);
		
		String featureTrees = "[";
		
		for(FeatureModel fm : featureModels) {
			Pair<String,String> ftree = featureBL.getFeatureTree(fm);
			featureTrees += "{ id : '" + fm.getId() + "' , nodes : "+ ftree.getFst() +" , links : " + ftree.getSnd() + " },";
		}
		
		featureTrees += "]";
		
		model.addAttribute("featureModels", featureModels);
		model.addAttribute("featureTrees", featureTrees);
		
		return "featureView/index";
	}
	
	@RequestMapping(value="{id}")
	public String showFeature(Model model,@PathVariable(name = "id",required = true) String id) {
	
		if(MainController.session().getAttribute("selectedSplId") == null)
			return "redirect:/";
		
		Feature f = featureBL.getFeatureById(id);
		
		if(f == null)
			return "redirect:/features";
		
		Pair<String,String> reducedFeatureTree = featureBL.getReducedFeatureTree(id);
		
		List<VariationPoint> vps = featureBL.getFeaturesVPs(id);
		
		// Connection cmap <-> spl
		List<LinkElement> cmapRelated = cmapBL.getFeatureRelatedLinkElements(id);
		List<Resource> resources = new ArrayList<>();
		for(LinkElement le : cmapRelated) {
			for(Resource r : cmapBL.getLinkElementRelatedResources(le.getId())) {
				resources.add(r);
			}
		}
		
		model.addAttribute("nodeList",reducedFeatureTree.getFst());
		model.addAttribute("linkList",reducedFeatureTree.getSnd());
		model.addAttribute("feature", f);
		model.addAttribute("resources",resources);
		model.addAttribute("cmapElements",cmapRelated);
		model.addAttribute("vps", vps);		
		
		return "featureView/feature";
	}

}

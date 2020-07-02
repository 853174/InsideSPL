package com.onekin.insideSpl.businessLogic.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onekin.insideSpl.businessLogic.CMapBLInterface;
import com.onekin.insideSpl.businessLogic.FeatureBLInterface;
import com.onekin.insideSpl.businessLogic.ProductBLInterface;
import com.onekin.insideSpl.dao.ProductDaoInterface;
import com.onekin.insideSpl.domain.Feature;
import com.onekin.insideSpl.domain.VariantComponent;
import com.onekin.insideSpl.domain.VariantFeature;
import com.onekin.insideSpl.domain.VariantModel;
import com.onekin.insideSpl.domain.cmap.CMap;
import com.onekin.insideSpl.domain.cmap.Concept;
import com.onekin.insideSpl.domain.cmap.LinkElement;
import com.onekin.insideSpl.utils.Pair;
import com.onekin.insideSpl.utils.Triplet;

@Service
public class ProductBLImplementation implements ProductBLInterface {

	@Autowired
	private ProductDaoInterface productDao;

	@Autowired
	private FeatureBLInterface featureBL;
	
	@Autowired
	private CMapBLInterface cmapBL;

	@Override
	public List<VariantModel> getAllProductsOfSpl(String splId) {
		return productDao.getAllProductsOfSpl(splId);
	}

	@Override
	public VariantModel getProductById(String id) {
		return productDao.getProductById(id);
	}

	@Override
	public List<VariantModel> orderBySelectedFeaturesSize(List<VariantModel> vm) {
		vm.sort(new ProductComparator());
		return vm;
	}
	
	@Override
	public String getProductConfigurationJsonById(String id) {
		//{ "id" : "ajson1", "type": "selected", "parent" : "#", "text" : "Simple root node" },
		
		VariantModel product = getProductById(id);

		if (product != null) {
			
			String json = "[";
			
			for(VariantComponent vc : product.getVariants()) {
				if(vc instanceof VariantFeature) {
					VariantFeature vf = (VariantFeature) vc;
					
					String featureJson = "{"
							+ "'id' : '" + vf.getFeature().getId() + "', "
							+ "'text' : '" + vf.getFeature().getName() + "', ";
					
					Feature p = featureBL.getParenFeatureOfFeature(vf.getFeature().getId());
					
					if(p == null) {
						featureJson += "'parent' : '#'";
					}else {
						featureJson += "'parent' : '" + p.getId() +"'";
					}
					
					if(vf.isSelected()) {
						featureJson += ",'type' : 'selected'";
					}else {
						featureJson += ",'type' : 'unselected'";
					}
					
					featureJson += "},";
					
					json += featureJson;
					
				}
			}
			
			json += "]";
			
			return json;
		}
		
		return null;
		
	}
	
	private String unselectedJson;
	private int unselectedSize;

	@Override
	public Triplet<String, Integer, Integer> getProductInfoById(String id) {
		
		unselectedJson = "";
		unselectedSize = 0;

		VariantModel product = getProductById(id);

		if (product != null) {
			List<Feature> rootFeatures = featureBL.getRootFeaturesOfSpl(product.getSpl().getId());

			int unselected = 0;
			int fromTotalSize = 0;

			String resultJson = "[";
			for (Feature root : rootFeatures) {
				Pair<String, Integer> rootR = infoRecursive(product, root, 0);
				resultJson += rootR.getFst();
				unselected += rootR.getSnd();
				fromTotalSize += featureBL.getFeatureSize(root.getId());
			}

			int selectedFeaturesSize = fromTotalSize - unselected;
			
			// Add unselected part
			resultJson += "{ name: 'unselected', value: " + unselectedSize + ", "
					+ "children: ["
					+ unselectedJson
					+ "]}";

			resultJson += "]";
			
			resultJson = "[{ id: 'root', name: '" + product.getFilename() + "', children: " + resultJson + "}]";

			Triplet<String, Integer, Integer> result = new Triplet<String, Integer, Integer>(resultJson,
					selectedFeaturesSize, fromTotalSize);
			return result;
		}

		return null;
	}

	private Pair<String, Integer> infoRecursive(VariantModel vm, Feature actual, int level) {
		
		int size = featureBL.getFeatureSize(actual.getId());

		int unselected;
		int selected;
		
		String children = "[";
		
		// Is feature selected?
		if(isFeatureSelected(vm, actual)) {
			// Yes: 
			//	unselected = 0 + sum(child.unselected);
			//	children = [...];
			
			unselected = 0;
			
			List<Feature> childs = featureBL.getChildFeaturesOfFeature(actual.getId());
			
			
			
			for (Feature child : childs) {
				Pair<String, Integer> childR = infoRecursive(vm, child, level + 1);
				children += childR.getFst();
				unselected += childR.getSnd();
			}
			
		}else {
			// No: 
			// 	unselected = size;
			// 	children = [];
			
			unselected = size;
		}
		
		children += "]";		
		
		selected = (size - unselected) >= 0 ? (size - unselected) : 0 ;
		
		// Fill info

		String json = "";
		
		if (level == 1) {
			
			// Selected part
			json = "{id: '" + actual.getId() + "', "
					+ "name: '" + actual.getName() + "', "
					+ "value: " + selected + ", "
					+ "children: " + children + "},";
			
			// Unselected part (keep it apart)
			unselectedJson += "{id: '" + actual.getId() + "', "
					+ "name: '" + actual.getName() + "', "
					+ "value: " + unselected + ","
					+ "normal: {fill: '#c5c5c5'}},";
			
			unselectedSize += unselected;
			
		} else {
			// Only selected part
			if(isFeatureSelected(vm, actual)) {
				json = "{id: '" + actual.getId() + "', "
						+ "name: '" + actual.getName() + "', "
						+ "value: " + selected + ", "
						+ "children: " + children + "},";
			}
		}
		
		Pair<String, Integer> result = new Pair<String, Integer>(json, unselected);

		return result;

	}

	private boolean isFeatureSelected(VariantModel vm, Feature f) {

		for (VariantComponent vc : vm.getVariants()) {
			if (vc instanceof VariantFeature) {
				VariantFeature vf = (VariantFeature) vc;
				if (vf.getFeature().getId().contentEquals(f.getId()))
					return vf.isSelected();
			}
		}

		return false;
	}

	private class ProductComparator implements Comparator<VariantModel> {

		@Override
		public int compare(VariantModel o1, VariantModel o2) {
			return getProductSize(o1) - getProductSize(o2);
		}

	}

	@Override
	public int getProductSize(VariantModel p) {
		int size = 0;
		for (VariantComponent vc : p.getVariants()) {
			if ((vc instanceof VariantFeature) && vc.isSelected())
				size++;
		}
		return size;
	}

	@Override
	public String compareProducts(VariantModel p1, VariantModel p2) {
		// { "similarity" : x, "same" : [{"id" : "..."},...], "diff" : [...] }
		
		String same = "'same' : [";
		String diff = "'diff' : [";
		float s = 0;
		float t = 0;
		
		for(Feature f : featureBL.getAllFeaturesOfSpl(p1.getSpl().getId())) {
			
			// Ignore mandatory features
			if(! f.getType().contentEquals("MANDATORY")) {
				boolean p1s = isFeatureSelected(p1, f);
				boolean p2s = isFeatureSelected(p2, f);
				
				if(p1s == p2s) {
					same += "{'id' : '"+ f.getId() +"'},";
					s++;
				}else {
					diff += "{'id' : '"+ f.getId() +"'},";
				}
				
				t++;
			}
			
		}
		
		same += "]";
		diff += "]";
		
		String json = "{"
				+ "'similarity' : " + String.format("%.02f", ((100 * s) / t)) + ","
				+ same + ","
				+ diff
				+ "}";
		
		return json;
	}
	
	@Override
	public List<VariantModel> orderByConceptCoverage(List<VariantModel> vm, CMap cmap) {
		
		for (int i = 0; i < vm.size(); i++) {
			VariantModel min = vm.get(i);
			int minId = i;
			for (int j = i + 1; j < vm.size(); j++) {
				if (getConceptCoverage(vm.get(j), cmap).size() < getConceptCoverage(min, cmap).size()) {
					min = vm.get(j);
					minId = j;
				}
			}
			
			VariantModel temp = vm.get(i);
			vm.set(i, min);
			vm.set(minId, temp);
		}
		
		return vm;
	}
	
	@Override
	public List<Concept> getConceptCoverage(VariantModel vm, CMap cmap) {
		List<Concept> concepts = new ArrayList<>();
		
		for(VariantComponent vc : vm.getVariants()) {
			if(vc instanceof VariantFeature && vc.isSelected()) {
				VariantFeature vf = (VariantFeature) vc;
				List<Concept> cs = cmapBL.getFeatureRelatedConceptsOnCMap(vf.getFeature().getId(), cmap.getId());
				for(Concept c : cs) {
					if(! concepts.contains(c))
						concepts.add(c);
				}
			}
		}
		
		return concepts;
	}

}

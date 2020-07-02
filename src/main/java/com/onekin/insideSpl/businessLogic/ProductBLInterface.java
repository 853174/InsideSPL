package com.onekin.insideSpl.businessLogic;

import java.util.List;

import com.onekin.insideSpl.domain.VariantModel;
import com.onekin.insideSpl.domain.cmap.CMap;
import com.onekin.insideSpl.domain.cmap.Concept;
import com.onekin.insideSpl.utils.Triplet;

public interface ProductBLInterface {
	
	public List<VariantModel> getAllProductsOfSpl(String splId);
	public VariantModel getProductById(String id);
	public List<VariantModel> orderBySelectedFeaturesSize(List<VariantModel> vm);
	
	public Triplet<String, Integer, Integer> getProductInfoById(String id);
	public int getProductSize(VariantModel p);
	public String getProductConfigurationJsonById(String id);
	public String compareProducts(VariantModel product1, VariantModel product2);

	public List<VariantModel> orderByConceptCoverage(List<VariantModel> vm, CMap cmap);
	public List<Concept> getConceptCoverage(VariantModel vm, CMap cmap);
	
}

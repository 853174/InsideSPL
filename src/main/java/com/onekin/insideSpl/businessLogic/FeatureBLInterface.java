package com.onekin.insideSpl.businessLogic;

import java.util.List;

import com.onekin.insideSpl.domain.Attribute;
import com.onekin.insideSpl.domain.Feature;
import com.onekin.insideSpl.domain.FeatureModel;
import com.onekin.insideSpl.domain.VariationPoint;
import com.onekin.insideSpl.utils.Pair;

public interface FeatureBLInterface {
	
	public List<FeatureModel> getAllFeatureModelsOfSpl(String splId);
	public FeatureModel getFeatureModelById(String id);
	public List<Feature> getAllFeaturesOfSpl(String splId);
	public Feature getFeatureById(String id);
	
	public List<Feature> getRootFeaturesOfSpl(String splId);
	public List<Feature> getChildFeaturesOfFeature(String featureId);
	
	public int getFeatureSize(String featureId);
	public Feature getParenFeatureOfFeature(String id);
	
	public Pair<String, String> getFeatureTree(FeatureModel fm);
	public Pair<String, String> getReducedFeatureTree(String id);
	
	public List<VariationPoint> getFeaturesVPs(String id);
	

}

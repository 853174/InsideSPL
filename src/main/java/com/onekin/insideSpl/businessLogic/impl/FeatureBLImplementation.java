package com.onekin.insideSpl.businessLogic.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onekin.insideSpl.businessLogic.FeatureBLInterface;
import com.onekin.insideSpl.dao.FeatureDaoInterface;
import com.onekin.insideSpl.domain.Attribute;
import com.onekin.insideSpl.domain.Feature;
import com.onekin.insideSpl.domain.FeatureModel;
import com.onekin.insideSpl.domain.VariationPoint;
import com.onekin.insideSpl.utils.Pair;

@Service
public class FeatureBLImplementation implements FeatureBLInterface {

	@Autowired
	private FeatureDaoInterface featureDao;

	@Override
	public List<FeatureModel> getAllFeatureModelsOfSpl(String splId) {
		return featureDao.getAllFeatureModelsOfSpl(splId);
	}

	@Override
	public FeatureModel getFeatureModelById(String id) {
		return featureDao.getFeatureModelById(id);
	}

	@Override
	public List<Feature> getAllFeaturesOfSpl(String splId) {
		return featureDao.getAllFeaturesOfSpl(splId);
	}

	@Override
	public Feature getFeatureById(String id) {
		return featureDao.getFeatureById(id);
	}

	@Override
	public List<Feature> getRootFeaturesOfSpl(String splId) {
		return featureDao.getRootFeaturesOfSpl(splId);
	}

	@Override
	public List<Feature> getChildFeaturesOfFeature(String featureId) {
		return featureDao.getChildFeaturesOfFeature(featureId);
	}

	@Override
	public int getFeatureSize(String featureId) {
		return featureDao.getFeatureSize(featureId);
	}

	@Override
	public Feature getParenFeatureOfFeature(String id) {
		return featureDao.getParenFeatureOfFeature(id);
	}

	@Override
	public Pair<String,String> getFeatureTree(FeatureModel fm){

		String nodeList = "[";
		String linkList = "[";

		for(Feature f : featureDao.getAllFeaturesOfFeatureModel(fm.getId())) {

			nodeList += "{ key : '" + f.getId() + "' , text : '" + f.getName() + "' , type : '" + f.getType() + "' },";

			Feature parent = getParenFeatureOfFeature(f.getId());
			if(parent != null) {
				linkList += "{ from: '" + parent.getId() + "' , to : '" + f.getId() + "' },";
			}


		}

		nodeList += "]";
		linkList += "]";

		return new Pair<>(nodeList,linkList);
	}

	@Override
	public Pair<String,String> getReducedFeatureTree(String id) {
		Feature f = featureDao.getFeatureById(id);
		Feature parent = featureDao.getParenFeatureOfFeature(id);
		List<Feature> children = featureDao.getChildFeaturesOfFeature(id);

		if( f == null )
			return null;

		String nodeList = "[";
		String linkList = "[";

		// Add feature's info
		nodeList += "{ key : '" + f.getId() + "' , text : '" + f.getName() + "' , type : '" + f.getType() + "'},";

		// Add parent's info (if exists)
		if(parent != null) {
			nodeList += "{ key : '" + parent.getId() + "' , text : '" + parent.getName() + "' , type : '" + parent.getType() + "'},";
			linkList += "{ from: '" + parent.getId() + "' , to : '" + f.getId() + "' },";
		}

		// Add children info (if any)
		for(Feature c : children) {
			nodeList += "{ key : '" + c.getId() + "' , text : '" + c.getName() + "' , type : '" + c.getType() + "'},";
			linkList += "{ from: '" + f.getId() + "' , to : '" + c.getId() + "' },";
		}

		// Close
		nodeList += "]";
		linkList += "]";

		return new Pair<>(nodeList,linkList);

	}

	@Override
	public List<VariationPoint> getFeaturesVPs(String id) {
		return featureDao.getFeaturesVPs(id);
	}

}

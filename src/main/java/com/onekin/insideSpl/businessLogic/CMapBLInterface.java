package com.onekin.insideSpl.businessLogic;

import java.util.List;

import com.onekin.insideSpl.domain.Feature;
import com.onekin.insideSpl.domain.cmap.*;

public interface CMapBLInterface {
	
	public List<CMap> getCMapsOfSpl(String splId);
	public CMap getCMapById(String id);
	
	public String getNodeString(CMap cmap);
	public String getLinkString(CMap cmap);
	public String getResourceString(CMap cmap);
	public String getRelatedFeaturesString(CMap cmap);
	
	public List<LinkElement> getFeatureRelatedLinkElements(String fId);
	public List<Feature> getLinkElementRelatedFeatures(String leId);
	public List<Resource> getLinkElementRelatedResources(String id);
	public List<Concept> getFeatureRelatedConceptsOnCMap(String id, String id2);

}

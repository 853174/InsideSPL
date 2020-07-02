package com.onekin.insideSpl.dao;

import java.util.List;

import com.onekin.insideSpl.domain.Feature;
import com.onekin.insideSpl.domain.cmap.CMap;
import com.onekin.insideSpl.domain.cmap.Concept;
import com.onekin.insideSpl.domain.cmap.LinkElement;
import com.onekin.insideSpl.domain.cmap.Resource;

public interface CMapDaoInterface {
	
	public List<CMap> getCMapsOfSpl(String splId);
	public CMap getCMapById(String id);
	
	public List<LinkElement> getFeatureRelatedLinkElements(String fId);
	public List<Feature> getLinkElementRelatedFeatures(String leId);
	public List<Resource> getLinkElementRelatedResources(String id);
	public List<Concept> getFeatureRelatedConceptsOnCMap(String fId, String cmapId);

}

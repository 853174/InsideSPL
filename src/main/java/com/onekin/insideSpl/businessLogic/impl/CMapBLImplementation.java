package com.onekin.insideSpl.businessLogic.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onekin.insideSpl.businessLogic.CMapBLInterface;
import com.onekin.insideSpl.dao.CMapDaoInterface;
import com.onekin.insideSpl.domain.Feature;
import com.onekin.insideSpl.domain.cmap.CMap;
import com.onekin.insideSpl.domain.cmap.Concept;
import com.onekin.insideSpl.domain.cmap.Connection;
import com.onekin.insideSpl.domain.cmap.LinkElement;
import com.onekin.insideSpl.domain.cmap.LinkingPhrase;
import com.onekin.insideSpl.domain.cmap.Resource;

@Service
public class CMapBLImplementation implements CMapBLInterface {
	
	@Autowired
	private CMapDaoInterface cmapDao;

	@Override
	public List<CMap> getCMapsOfSpl(String splId) {
		return cmapDao.getCMapsOfSpl(splId);
	}

	@Override
	public CMap getCMapById(String id) {
		return cmapDao.getCMapById(id);
	}

	@Override
	public String getNodeString(CMap cmap) {
		// { key : ID , text : LABEL }
		
		String r = "[";
		
		for(Concept c : cmap.getConcepts()) {
			r += "{ key : '" + c.getId() + "', text : '" + c.getLabel() +"' },";
		}
		
		r += "]";
		
		return r;
	}

	@Override
	public String getLinkString(CMap cmap) {
		// { key: LinkingPhrase.ID , from : FROM_CONCEPT , to : TO_CONCEPT , text : LABEL }
		String r = "[";
		
		for(Connection c : cmap.getConnections()) {
			
			LinkElement from = c.getFrom();
			LinkElement to = c.getTo();
			
			// A: Concept --> LinkingPhrase
			if(from instanceof Concept && to instanceof LinkingPhrase) {
				String phrase = ((LinkingPhrase) to).getLabel();
				for(Concept co : resolveConnection(cmap, (LinkingPhrase) to)){
					r += "{ key : '" + to.getId() + "' , from : '" + from.getId() + "', to : '" + co.getId() +"' , text : '" + phrase +"' },";
				}
			}
			
			// B: LinkingPhrase --> Concept
			// Ignore. Those connections are processed in the previous 'resolveConnection'
		}
		
		r += "]";
		
		return r;
	}
	
	private List<Concept> resolveConnection(CMap cmap, LinkingPhrase lp){
		List<Concept> r = new ArrayList<Concept>();
		
		for(Connection c : cmap.getConnections()) {
			
			LinkElement from = c.getFrom();
			LinkElement to = c.getTo();
			
			if( from instanceof LinkingPhrase && to instanceof Concept) {
				if( ((LinkingPhrase) from).getId().contentEquals(lp.getId()))
					r.add((Concept) to);
					
			}
		}
		
		return r;
	}

	@Override
	public String getResourceString(CMap cmap) {
		// {key: ID , parent: PARENT_ID , label: LABEL , description: DESCRIPTION, url: URL}
		
		String r = "[";
		
		for(Resource rs : cmap.getResources()) {
			r += "{ key : '" + rs.getId() + "', parent : '" + rs.getParent().getId() + "' , label : '" + rs.getLabel() +"' , description : '" + rs.getDescription() + "' , url : '" + rs.getUrl() + "' },";
		}
		
		r += "]";
		
		return r;
	}

	@Override
	public String getRelatedFeaturesString(CMap cmap) {
		// { key: ID , feature_id : fId , feature_name : fName }
		String r = "[";
		
		List<LinkElement> allLinkElements = new ArrayList<>();
		allLinkElements.addAll(cmap.getConcepts());
		allLinkElements.addAll(cmap.getLinkingPhrases());
		
		for(LinkElement le : allLinkElements) {
			for(Feature f : le.getConnFeatures()) {
				r += "{ key : '" + le.getId() + "' , feature_id : '" + f.getId() + "' , feature_name : '" + f.getName() + "' },";
			}
		}
		
		r += "]";
		return r;
	}

	@Override
	public List<LinkElement> getFeatureRelatedLinkElements(String fId) {
		return cmapDao.getFeatureRelatedLinkElements(fId);
	}

	@Override
	public List<Feature> getLinkElementRelatedFeatures(String leId) {
		return cmapDao.getLinkElementRelatedFeatures(leId);
	}

	@Override
	public List<Resource> getLinkElementRelatedResources(String id) {
		return cmapDao.getLinkElementRelatedResources(id);
	}

	@Override
	public List<Concept> getFeatureRelatedConceptsOnCMap(String fId, String cmapId) {
		return cmapDao.getFeatureRelatedConceptsOnCMap(fId,cmapId);
	}

}

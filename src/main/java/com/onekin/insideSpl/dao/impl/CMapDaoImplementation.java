package com.onekin.insideSpl.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.onekin.insideSpl.dao.CMapDaoInterface;
import com.onekin.insideSpl.dao.FeatureDaoInterface;
import com.onekin.insideSpl.domain.Feature;
import com.onekin.insideSpl.domain.cmap.CMap;
import com.onekin.insideSpl.domain.cmap.Concept;
import com.onekin.insideSpl.domain.cmap.Connection;
import com.onekin.insideSpl.domain.cmap.LinkElement;
import com.onekin.insideSpl.domain.cmap.LinkingPhrase;
import com.onekin.insideSpl.domain.cmap.Resource;

@Repository
public class CMapDaoImplementation implements CMapDaoInterface {
	
	@Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	@Autowired
	private FeatureDaoInterface featureDao;
	
	// QUERIES
	private final static String GET_CMAPS_OF_SPL = "SELECT C.* FROM CMAP C INNER JOIN SPL S ON C.SPL_ID = S.ID WHERE S.ID=':spl_id'";
	private final static String GET_CMAP_BY_ID = "SELECT ID,NAME FROM CMAP WHERE ID=':id'";
	private final static String GET_CONCEPTS_OF_CMAP = "SELECT * FROM CONCEPT C NATURAL JOIN LINK_ELEMENT LE WHERE CMAP=':cmap_id'";
	private final static String GET_CONCEPT_BY_ID = "SELECT * FROM CONCEPT C NATURAL JOIN LINK_ELEMENT LE WHERE ID=':id'";
	private final static String GET_LINKING_PHRASES_OF_CMAP = "SELECT * FROM LINKING_PHRASE LP NATURAL JOIN LINK_ELEMENT LE WHERE CMAP=':cmap_id'";
	private final static String GET_LINKING_PHRASE_BY_ID = "SELECT * FROM LINKING_PHRASE LP NATURAL JOIN LINK_ELEMENT LE WHERE ID=':id'";
	private final static String GET_CONNECTIONS_OF_CMAP = "SELECT * FROM CONNECTION WHERE CMAP=':cmap_id'";
	private final static String GET_RESOURCES_OF_CMAP = "SELECT * FROM RESOURCE WHERE CMAP=':cmap_id'";
	private final static String GET_RESOURCES_OF_LINK_ELEMENT = "SELECT * FROM RESOURCE WHERE PARENT_ID=':link_element_id'";
	
	// SPL connection
	private final static String GET_RELATED_ELEMENTS_BY_FEATURE_ID = "SELECT LINK_ELEMENT_ID FROM FEATURE_LINK_ELEMENT WHERE FEATURE_ID=':feature_id'";
	private final static String GET_RELATED_ELEMENTS_BY_FEATURE_ID_AND_CMAP = "SELECT FLE.LINK_ELEMENT_ID FROM FEATURE_LINK_ELEMENT FLE INNER JOIN LINK_ELEMENT LE ON LE.ID = FLE.LINK_ELEMENT_ID WHERE FLE.FEATURE_ID=':feature_id' AND LE.CMAP=':cmap_id'";
	
	@Override
	public List<CMap> getCMapsOfSpl(String splId) {
		List<CMap> cmaps = new ArrayList<CMap>();
		
		for(CMap cmap : namedJdbcTemplate.query(GET_CMAPS_OF_SPL.replace(":spl_id", splId),new CMapRowMapper())) {
			cmaps.add(completeCMap(cmap));
		}
		
		return cmaps;
	}

	@Override
	public CMap getCMapById(String id) {
		
		for(CMap cmap : namedJdbcTemplate.query(GET_CMAP_BY_ID.replace(":id", id),new CMapRowMapper())) {
			// Return first (and, in theory, unique one)
			return completeCMap(cmap);
		}
		
		// There is no CMap with that id
		return null;
	}
	
	@Override
	public List<Feature> getLinkElementRelatedFeatures(String leId){
		return featureDao.getLinkElementRelatedFeatures(leId);
	}
	
	@Override
	public List<LinkElement> getFeatureRelatedLinkElements(String fId){
		String q = GET_RELATED_ELEMENTS_BY_FEATURE_ID.replace(":feature_id", fId);
		List<LinkElement> result = new ArrayList<>();
		for(String leId : namedJdbcTemplate.query(q, new StringRowMapper())) {
			LinkElement le = findLinkElement(leId);
			if(le != null)
				result.add(le);
		}
			
		return result;
	}
	
	@Override
	public List<Concept> getFeatureRelatedConceptsOnCMap(String fId, String cmapId) {
		
		String q = GET_RELATED_ELEMENTS_BY_FEATURE_ID_AND_CMAP
				.replace(":feature_id", fId)
				.replace(":cmap_id",cmapId);
		
		List<Concept> result = new ArrayList<>();
		
		for(String id : namedJdbcTemplate.query(q, new StringRowMapper())) {
			LinkElement le = findLinkElement(id);
			if(le instanceof Concept) {
				result.add((Concept) le);
			}
		}
		
		return result;
	}
	
	@Override
	public List<Resource> getLinkElementRelatedResources(String id) {
		String q = GET_RESOURCES_OF_LINK_ELEMENT.replace(":link_element_id", id);
		return namedJdbcTemplate.query(q, new ResourceRowMapper());
	}
	
	private CMap completeCMap(CMap cmap) {
		// 1: Add Concepts
		for(Concept c : namedJdbcTemplate.query(GET_CONCEPTS_OF_CMAP.replace(":cmap_id", cmap.getId()), new ConceptRowMapper())) {
			// Feature <-> Concept relation
			c.setConnFeatures(featureDao.getLinkElementRelatedFeatures(c.getId()));
			
			cmap.addConcept(c);
		}
		
		// 2: Add LinkingPhrases
		for(LinkingPhrase lp : namedJdbcTemplate.query(GET_LINKING_PHRASES_OF_CMAP.replace(":cmap_id", cmap.getId()), new LinkingPhraseRowMapper())) {
			// Feature <-> LinkingPhrase relation
			lp.setConnFeatures(featureDao.getLinkElementRelatedFeatures(lp.getId()));
			
			cmap.addLinkingPhrase(lp);
		}
		
		// 3: Add Connections
		for(Connection c : namedJdbcTemplate.query(GET_CONNECTIONS_OF_CMAP.replace(":cmap_id", cmap.getId()), new ConnectionRowMapper())) {
			cmap.addConnection(c);
		}
		
		// 4: Add Resources
		for(Resource r : namedJdbcTemplate.query(GET_RESOURCES_OF_CMAP.replace(":cmap_id", cmap.getId()), new ResourceRowMapper())) {
			cmap.addResource(r);
		}
		
		return cmap;
	}
	
	private LinkElement findLinkElement(String id) {
		LinkElement r;
		
		for(Concept c : namedJdbcTemplate.query(GET_CONCEPT_BY_ID.replace(":id", id),new ConceptRowMapper())) {
			// If it's here [id]->Concept
			return c;
		}
		
		for(LinkingPhrase lp : namedJdbcTemplate.query(GET_LINKING_PHRASE_BY_ID.replace(":id", id),new LinkingPhraseRowMapper())) {
			// If it's here [id]->LinkingPhrase
			return lp;
		}
		
		// It's nothing
		return null;
	}

	// ROWMAPPERS	
	private class CMapRowMapper implements RowMapper<CMap>{

		@Override
		public CMap mapRow(ResultSet rs, int rowNum) throws SQLException {
			String id = rs.getString(1);
			String name = rs.getString(2);
			return new CMap(id, name);
		}
		
	}
	
	private class ConceptRowMapper implements RowMapper<Concept>{

		@Override
		public Concept mapRow(ResultSet rs, int rowNum) throws SQLException {
			String id = rs.getString(1);
			String label = rs.getString(2);
			return new Concept(id, label);
		}
		
	}
	
	private class LinkingPhraseRowMapper implements RowMapper<LinkingPhrase>{

		@Override
		public LinkingPhrase mapRow(ResultSet rs, int rowNum) throws SQLException {
			String id = rs.getString(1);
			String label = rs.getString(2);
			return new LinkingPhrase(id, label);
		}
		
	}
	
	private class ConnectionRowMapper implements RowMapper<Connection>{

		@Override
		public Connection mapRow(ResultSet rs, int rowNum) throws SQLException {
			String id = rs.getString(1);
			String from_id = rs.getString(2);
			String to_id = rs.getString(3);
			
			LinkElement from = findLinkElement(from_id);
			LinkElement to = findLinkElement(to_id);
			
			if(from == null || to == null)
				return null;
			
			return new Connection(to_id, from, to);
		}
		
	}
	
	private class ResourceRowMapper implements RowMapper<Resource>{

		@Override
		public Resource mapRow(ResultSet rs, int rowNum) throws SQLException {
			String id = rs.getString(1);
			String label = rs.getString(2);
			String description = rs.getString(3);
			String url = rs.getString(4);
			String parent_id = rs.getString(5);
			
			LinkElement parent = findLinkElement(parent_id);
			
			if(parent == null)
				return null;
			
			return new Resource(id, label, description, url, parent);
		}
		
	}
	
	private class StringRowMapper implements RowMapper<String>{

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString(1);
		}
		
	}
	
}

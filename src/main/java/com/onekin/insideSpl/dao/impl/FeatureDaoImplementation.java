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
import com.onekin.insideSpl.dao.GenericDaoInterface;
import com.onekin.insideSpl.dao.VariationPointDaoInterface;
import com.onekin.insideSpl.domain.Attribute;
import com.onekin.insideSpl.domain.Feature;
import com.onekin.insideSpl.domain.FeatureModel;
import com.onekin.insideSpl.domain.SPL;
import com.onekin.insideSpl.domain.VariationPoint;
import com.onekin.insideSpl.domain.cmap.LinkElement;

@Repository
public class FeatureDaoImplementation implements FeatureDaoInterface {

	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	private GenericDaoInterface genericDao;

	@Autowired
	private VariationPointDaoInterface variationPointDao;
	
	// QUERIES
	private static final String GET_ALL_FEATURE_MODELS_OF_SPL = "SELECT * FROM FEATURE_MODEL WHERE SPL=':spl_id'";
	private static final String GET_FEATURE_MODEL_BY_ID = "SELECT * FROM FEATURE_MODEL WHERE ID=':id'";
	private static final String GET_ALL_FEATURES_OF_SPL = "SELECT F.* FROM FEATURE F INNER JOIN FEATURE_MODEL FM ON F.FEATURE_MODEL = FM.ID WHERE FM.SPL=':splId'";
	private static final String GET_FEATURE_BY_ID = "SELECT * FROM FEATURE WHERE ID=':id'";
	private static final String GET_ROOT_FEATURES = "SELECT F.* FROM FEATURE F INNER JOIN FEATURE_MODEL FM ON F.FEATURE_MODEL = FM.ID WHERE PARENT IS NULL AND FM.SPL=':splId'";
	private static final String GET_FETURES_CHILDS = "SELECT C.* FROM FEATURE P INNER JOIN FEATURE C ON P.ID=C.PARENT WHERE P.ID=':parentId'";
	private static final String GET_FETURES_PARENT = "SELECT P.* FROM FEATURE P INNER JOIN FEATURE C ON P.ID=C.PARENT WHERE C.ID=':childId'";
	private static final String GET_FEATURE_SIZE = "SELECT SIZE FROM FEATURE_SIZE WHERE FEATURE_ID=':id'";
	private static final String GET_FEATURE_ATTRIBUTES = "SELECT A.* FROM ATTRIBUTE A INNER JOIN FEATURE F ON F.ID=A.TARGET_FEATURE WHERE F.ID=':id'";
	private static final String GET_FEATURES_VP_IDS = "SELECT VP.ID FROM VARIATION_POINT_FEATURE VPF INNER JOIN VARIATION_POINT VP ON VPF.VP_ID=VP.ID WHERE FEATURE_ID=':id' AND EXPRESION LIKE '%:feature_name%' ORDER BY CODE_ELEMENT_ID ASC";
	
	// CMap connection
	private final static String GET_RELATED_FEATURES_BY_ELEMENT_ID = "SELECT F.* FROM FEATURE_LINK_ELEMENT FLE INNER JOIN FEATURE F ON FLE.FEATURE_ID=F.ID WHERE FLE.LINK_ELEMENT_ID=':link_element_id'";
	
	@Override
	public List<FeatureModel> getAllFeatureModelsOfSpl(String splId) {
		String q = GET_ALL_FEATURE_MODELS_OF_SPL.replace(":spl_id", splId);
		return namedJdbcTemplate.query(q, new FeatureModelRowMapper());
	}
	
	@Override
	public FeatureModel getFeatureModelById(String id) {
		String q = GET_FEATURE_MODEL_BY_ID.replace(":id", id);
		for(FeatureModel fm : namedJdbcTemplate.query(q, new FeatureModelRowMapper())) {
			return fm;
		}

		return null;
	}
	
	
	@Override
	public List<Feature> getAllFeaturesOfSpl(String splId) {
		String q = GET_ALL_FEATURES_OF_SPL.replace(":splId", splId);
		return namedJdbcTemplate.query(q, new FeatureRowMapper());
	}
	
	@Override
	public List<Feature> getAllFeaturesOfFeatureModel(String fmId){
		FeatureModel fm = getFeatureModelById(fmId);
		List<Feature> result = new ArrayList<>();
		for(Feature f : getAllFeaturesOfSpl(fm.getSpl().getId())) {
			if(f.getFeatureModel().getId().contentEquals(fmId))
				result.add(f);
		}
		
		return result;
	}

	@Override
	public Feature getFeatureById(String id) {
		String q = GET_FEATURE_BY_ID.replace(":id", id);
		List<Feature> f = namedJdbcTemplate.query(q, new FeatureRowMapper());
		if (!f.isEmpty()) {
			return f.get(0);
		}

		return null;
	}

	@Override
	public List<Feature> getRootFeaturesOfSpl(String splId) {
		String q = GET_ROOT_FEATURES.replace(":splId",splId);
		return namedJdbcTemplate.query(q, new FeatureRowMapper());
	}

	@Override
	public List<Feature> getChildFeaturesOfFeature(String featureId) {
		String q = GET_FETURES_CHILDS.replace(":parentId", featureId);
		return namedJdbcTemplate.query(q, new FeatureRowMapper());
	}
	
	@Override
	public int getFeatureSize(String featureId) {
		String q = GET_FEATURE_SIZE.replace(":id", featureId);
		List<Integer> r = namedJdbcTemplate.query(q, new IntegerRowMapper());
		if(!r.isEmpty()) {
			return r.get(0);
		}
		
		return -1;
	}
	
	@Override
	public Feature getParenFeatureOfFeature(String id) {
		String q = GET_FETURES_PARENT.replace(":childId", id);
		List<Feature> fs = namedJdbcTemplate.query(q, new FeatureRowMapper());
		if(! fs.isEmpty()) {
			return fs.get(0);
		}
		
		return null;
	}
	
	@Override
	public List<Feature> getLinkElementRelatedFeatures(String leid){
		String q = GET_RELATED_FEATURES_BY_ELEMENT_ID.replace(":link_element_id", leid);
		return namedJdbcTemplate.query(q, new FeatureRowMapper());
	}
	
	private List<Attribute> getFeaturesAttributes(String id){
		String q = GET_FEATURE_ATTRIBUTES.replace(":id", id);
		return namedJdbcTemplate.query(q, new AttributeRowMapper());
	}
	
	@Override
	public List<VariationPoint> getFeaturesVPs(String id) {
		Feature f = getFeatureById(id);
		String q = GET_FEATURES_VP_IDS
				.replace(":id", id)
				.replace(":feature_name", f.getName());
		List<VariationPoint> result = new ArrayList<VariationPoint>();
		for(String vpId : namedJdbcTemplate.query(q,new StringRowMapper())) {
			VariationPoint vp = variationPointDao.getVariationPointById(vpId);
			if( vp != null) {
				result.add(vp);
			}else {
				System.err.println("Error finding VP with id " + vpId + " for feature " + id);
			}
		}
		return result;
	}

	// ROWMAPPERS
	
	private class IntegerRowMapper implements RowMapper<Integer>{

		@Override
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getInt(1);
		}
		
	}

	private class FeatureModelRowMapper implements RowMapper<FeatureModel> {

		@Override
		public FeatureModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			String id = rs.getString(1);
			String filename = rs.getString(2);
			String path = rs.getString(3);
			String splId = rs.getString(4);

			SPL spl = genericDao.getSplById(splId);

			return new FeatureModel(id, filename, path, spl);
		}

	}

	private class FeatureRowMapper implements RowMapper<Feature> {

		@Override
		public Feature mapRow(ResultSet rs, int rowNum) throws SQLException {
			String id = rs.getString(1);
			String name = rs.getString(2);
			String type = rs.getString(3);
			
			String featureModelId = rs.getString(5);

			// Don't get parent nor children
			// String parentId = rs.getString(4);
			Feature parent = null;

			Feature f = new Feature(id, name, type, parent, getFeatureModelById(featureModelId));
			
			f.setAttributes(getFeaturesAttributes(id));
			
			return f;
		}

	}
	
	private class AttributeRowMapper implements RowMapper<Attribute>{

		@Override
		public Attribute mapRow(ResultSet rs, int rowNum) throws SQLException {
			String id = rs.getString(1);
			String name = rs.getString(2);
			String type = rs.getString(3);
			String value = rs.getString(4);
			
			return new Attribute(id, name, type, value);
		}
		
	}
	
	private class StringRowMapper implements RowMapper<String>{

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString(1);
		}
		
	}

}

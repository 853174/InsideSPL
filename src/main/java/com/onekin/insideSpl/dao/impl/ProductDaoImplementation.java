package com.onekin.insideSpl.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.onekin.insideSpl.dao.CodeElementDaoInterface;
import com.onekin.insideSpl.dao.FeatureDaoInterface;
import com.onekin.insideSpl.dao.GenericDaoInterface;
import com.onekin.insideSpl.dao.ProductDaoInterface;
import com.onekin.insideSpl.domain.CodeElement;
import com.onekin.insideSpl.domain.Feature;
import com.onekin.insideSpl.domain.SPL;
import com.onekin.insideSpl.domain.VariantCode;
import com.onekin.insideSpl.domain.VariantComponent;
import com.onekin.insideSpl.domain.VariantFeature;
import com.onekin.insideSpl.domain.VariantModel;

@Repository
public class ProductDaoImplementation implements ProductDaoInterface {
	
	@Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	@Autowired 
	private GenericDaoInterface genericDao;
	
	@Autowired
	private FeatureDaoInterface featureDao;
	
	@Autowired
	private CodeElementDaoInterface codeElementDao;
	
	// QUERIES
	private static String GET_ALL_PRODUCTS_OF_SPL = "SELECT * FROM VARIANT_MODEL WHERE SPL_ID=':splId'";
	private static String GET_PRODUCT_BY_ID = "SELECT * FROM VARIANT_MODEL WHERE ID=':id'";
	private static String GET_VARIANT_CODES_BY_PRODUCT_ID = "SELECT VC.ID,VC.IS_SELECTED,CODE_ELEMENT_ID FROM VARIANT_COMPONENT VC INNER JOIN VARIANT_CODE VCODE ON VC.ID=VCODE.VC_ID WHERE VC.VARIANT_MODEL=':vmId'";
	private static String GET_VARIANT_FEATURES_BY_PRODUCT_ID = "SELECT VC.ID,VC.IS_SELECTED,FEATURE_ID FROM VARIANT_COMPONENT VC INNER JOIN VARIANT_FEATURE VF ON VC.ID=VF.VC_ID WHERE VC.VARIANT_MODEL=':vmId'";

	@Override
	public List<VariantModel> getAllProductsOfSpl(String splId) {
		String q = GET_ALL_PRODUCTS_OF_SPL.replace(":splId", splId);
		List<VariantModel> variantModels = new ArrayList<>();
		for(VariantModel vm : namedJdbcTemplate.query(q,new ProductRowMapper())) {
			variantModels.add(getProductById(vm.getId()));
		}
		return variantModels;
	}

	@Override
	public VariantModel getProductById(String id) {
		String q = GET_PRODUCT_BY_ID.replace(":id", id);
		List<VariantModel> vm = namedJdbcTemplate.query(q,new ProductRowMapper());
		if(! vm.isEmpty()) {
			
			VariantModel r = vm.get(0);
			List<VariantComponent> vcomponents = new ArrayList<>();
			
			// Get VariantFeatures
			String q2 = GET_VARIANT_FEATURES_BY_PRODUCT_ID.replace(":vmId", r.getId());
			List<VariantFeature> vfs = namedJdbcTemplate.query(q2, new VariantFeatureRowMapper());
			for(VariantFeature vf : vfs) {
				vf.setVariantModel(r);
				vcomponents.add(vf);
			}
			
			
			// Get VariantCodes
			q2 = GET_VARIANT_CODES_BY_PRODUCT_ID.replace(":vmId", r.getId());
			List<VariantCode> vcs = namedJdbcTemplate.query(q2, new VariantCodeRowMapper());
			for(VariantCode vc : vcs) {
				vc.setVariantModel(r);
				vcomponents.add(vc);
			}
			
			// Add VariantComponent list to result
			r.setVariants(vcomponents);
			
			return r;
			
		}
		return null;
	}
	
	
	private class ProductRowMapper implements RowMapper<VariantModel>{

		@Override
		public VariantModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			// ID, FILENAME, PATH, SPL_ID
			String id = rs.getString(1);
			String filename = rs.getString(2);
			String path = rs.getString(3);
			String splId = rs.getString(4);
			
			SPL spl = genericDao.getSplById(splId);
			
			VariantModel vm = new VariantModel(id, filename, path, spl);
			
			return vm;
		}
		
	}
	
	private class VariantFeatureRowMapper implements RowMapper<VariantFeature>{

		@Override
		public VariantFeature mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			String id = rs.getString(1);
			Boolean isSelected = rs.getBoolean(2);
			String featureId = rs.getString(3);
			
			Feature f = featureDao.getFeatureById(featureId);
			
			return new VariantFeature(id, isSelected, null, f);
		}
		
	}
	
	private class VariantCodeRowMapper implements RowMapper<VariantCode>{

		@Override
		public VariantCode mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			String id = rs.getString(1);
			Boolean isSelected = rs.getBoolean(2);
			String codeElementId = rs.getString(3);
			
			CodeElement ce = codeElementDao.getCodeElementById(codeElementId);
			
			return new VariantCode(id, isSelected, null, ce);
		}
		
	}

}

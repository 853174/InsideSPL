package com.onekin.insideSpl.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.onekin.insideSpl.dao.CodeElementDaoInterface;
import com.onekin.insideSpl.dao.VariationPointDaoInterface;
import com.onekin.insideSpl.domain.CodeElement;
import com.onekin.insideSpl.domain.CodeFile;
import com.onekin.insideSpl.domain.Code_VariationPoint;
import com.onekin.insideSpl.domain.VariationPoint;

@Repository
public class VariationPointDaoImplementation implements VariationPointDaoInterface{

	@Autowired
	private CodeElementDaoInterface codeElementDao;
	
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	// SELECT VPF.*,VP.* FROM (FEATURE F INNER JOIN VARIATION_POINT_FEATURE VPF ON F.ID = VPF.FEATURE_ID) INNER JOIN VARIATION_POINT VP ON VPF.VP_ID = VP.ID;
	
	private static final String GET_ALL_VARIATION_POINTS_OF_SPL = "SELECT VP.* FROM VARIATION_POINT VP INNER JOIN CODE_ELEMENT CE ON VP.CODE_ELEMENT_ID=CE.ID WHERE CE.SPL_ID=':spl_id'";
	private static final String GET_VARIATION_POINT_BY_ID = "SELECT * FROM VARIATION_POINT WHERE ID=':id'";
	private static final String GET_CODE_VARIATION_POINT_BY_ID = "SELECT * FROM VARIATION_POINT VP INNER JOIN CODE_VARIATION_POINT CVP ON VP.ID=CVP.VP_ID WHERE VP.ID=':id'";
	
	@Override
	public VariationPoint getVariationPointById(String id) {
		String q = GET_VARIATION_POINT_BY_ID.replace(":id", id);
		for(VariationPoint vp : namedJdbcTemplate.query(q, new VPRowMapper())) {
			return vp;
		}
		return null;
	}
	
	private Code_VariationPoint getCodeVariationPointById(String id) {
		String q = GET_CODE_VARIATION_POINT_BY_ID.replace(":id", id);
		for(Code_VariationPoint cvp : namedJdbcTemplate.query(q, new Code_VPRowMapper())) {
			return cvp;
		}
		
		return null;
	}
	
	// ROWMAPPERS
	
	private class VPRowMapper implements RowMapper<VariationPoint>{

		@Override
		public VariationPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
			String id = rs.getString(1);
			String codeElementId = rs.getString(2);
			String expresion = rs.getString(3);
			int vpSize = rs.getInt(4);
			
			CodeElement file = codeElementDao.getCodeElementById(codeElementId);
			
			if(file instanceof CodeFile) {
				return getCodeVariationPointById(id);
			}
			
			return new VariationPoint(id, expresion, file, vpSize);
		}
		
	}
	
	private class Code_VPRowMapper implements RowMapper<Code_VariationPoint>{
		
		@Override
		public Code_VariationPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			String id = rs.getString(1);
			String codeElementId = rs.getString(2);
			String expresion = rs.getString(3);
			int vpSize = rs.getInt(4);
			int startLine = rs.getInt(6);
			int endLine = rs.getInt(7);
			String content = rs.getString(8).replaceAll("#LINE_BREAK#", "\n");
			
			CodeFile file = (CodeFile) codeElementDao.getCodeElementById(codeElementId);
			
			return new Code_VariationPoint(id, startLine, endLine, expresion, file, vpSize, content);
		}
		
	}

}

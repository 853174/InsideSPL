package com.onekin.insideSpl.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.onekin.insideSpl.dao.GenericDaoInterface;
import com.onekin.insideSpl.domain.SPL;

@Repository
public class GenericDaoImplementation implements GenericDaoInterface {
	
	@Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	private SplRowMapper splRowMapper = new SplRowMapper();
	private SPL spl;
	
	// QUERIES
	private static String GET_ALL_SPL = "SELECT S.ID,S.NAME,GS.URL,GS.LAST_CHANGED FROM SPL S NATURAL JOIN GIT_SPL GS;";
	private static String GET_SPL_BY_ID = "SELECT S.ID,S.NAME,GS.URL,GS.LAST_CHANGED FROM SPL S NATURAL JOIN GIT_SPL GS WHERE S.ID=':id'";

	@Override
	public List<SPL> getAllSpl() {
		List<SPL> spls = namedJdbcTemplate.query(GET_ALL_SPL,new SplRowMapper());
		return spls;
	}

	@Override
	public SPL getSplById(String id) {
		
		if(spl != null) {
			if(spl.getId().contentEquals(id)) {
				return spl;
			}
		}
		
		String q = GET_SPL_BY_ID.replace(":id", id);
		List<SPL> spls = namedJdbcTemplate.query(q,splRowMapper);
		if(!spls.isEmpty()) {
			spl = spls.get(0);
			return spl;
		}
		
		return null;
	}
	
	// ROWMAPERS
	
	private class SplRowMapper implements RowMapper<SPL>{

		@Override
		public SPL mapRow(ResultSet rs, int rowNum) throws SQLException {
			String id = rs.getString(1);
			String name = rs.getString(2);
			String url = rs.getString(3);
			String lastChange = rs.getString(4);
			
			return new SPL(id,name,lastChange,url);
		}
		
	}

}

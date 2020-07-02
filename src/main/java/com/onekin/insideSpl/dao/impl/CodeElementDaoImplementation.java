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
import com.onekin.insideSpl.dao.GenericDaoInterface;
import com.onekin.insideSpl.domain.CodeElement;
import com.onekin.insideSpl.domain.CodeFile;
import com.onekin.insideSpl.domain.Directory;
import com.onekin.insideSpl.domain.Part;
import com.onekin.insideSpl.domain.SPL;

@Repository
public class CodeElementDaoImplementation implements CodeElementDaoInterface {
	
	@Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	@Autowired
	private GenericDaoInterface genericDao;

	// QUERIES
	private static String GET_ALL_DIRECTORIES_OF_SPL = "SELECT * FROM CODE_ELEMENT CE NATURAL JOIN DIRECTORY D WHERE CE.SPL_ID=':splId'";
	private static String GET_ALL_PARTS_OF_SPL = "SELECT * FROM CODE_ELEMENT CE NATURAL JOIN PART P WHERE CE.SPL_ID=':splId'";
	private static String GET_ALL_CODEFILES_OF_SPL = "SELECT * FROM CODE_ELEMENT CE NATURAL JOIN CODEFILE CF WHERE CE.SPL_ID=':splId'";
	
	private static String GET_DIRECTORY_BY_ID = "SELECT * FROM CODE_ELEMENT CE NATURAL JOIN DIRECTORY D WHERE ID=':id'";
	private static String GET_PART_BY_ID = "SELECT * FROM CODE_ELEMENT CE NATURAL JOIN PART P WHERE ID=':id'";
	private static String GET_CODEFILE_BY_ID = "SELECT * FROM CODE_ELEMENT CE NATURAL JOIN CODEFILE CF WHERE ID=':id'";
	
	@Override
	public List<CodeElement> getAllCodeElementsOfSpl(String splId) {
		List<CodeElement> ces = new ArrayList<>();
		
		List<Directory> ds = namedJdbcTemplate.query(
				GET_ALL_DIRECTORIES_OF_SPL.replace(":splId", splId),
				new DirectoryRowMapper());
		
		ces.addAll(ds);
		
		List<Part> ps = namedJdbcTemplate.query(
				GET_ALL_PARTS_OF_SPL.replace(":splId", splId),
				new PartRowMapper());
		
		ces.addAll(ps);
		
		List<CodeFile> cfs = namedJdbcTemplate.query(
				GET_ALL_CODEFILES_OF_SPL.replace(":splId", splId),
				new CodeFileRowMapper());
		
		ces.addAll(cfs);
		
		return ces;
	}

	@Override
	public CodeElement getCodeElementById(String id) {
		
		List<Directory> ds = namedJdbcTemplate.query(
				GET_DIRECTORY_BY_ID.replace(":id", id),
				new DirectoryRowMapper());
		
		if(! ds.isEmpty())
			return ds.get(0);
		
		List<Part> ps = namedJdbcTemplate.query(
				GET_PART_BY_ID.replace(":id", id),
				new PartRowMapper());
		
		if(! ps.isEmpty())
			return ps.get(0);
		
		List<CodeFile> cfs = namedJdbcTemplate.query(
				GET_CODEFILE_BY_ID.replace(":id", id),
				new CodeFileRowMapper());
		
		if(! cfs.isEmpty())
			return cfs.get(0);
		
		return null;
	}
	
	// ROWMAPPERS
	
	private class DirectoryRowMapper implements RowMapper<Directory>{

		@Override
		public Directory mapRow(ResultSet rs, int rowNum) throws SQLException {
			// ID,PATH,TYPE,PARENT,SPL_ID
			String id = rs.getString(1);
			String path = rs.getString(2);
			String type = rs.getString(3);
			String parentId = rs.getString(4);
			String splId = rs.getString(5);
			
			CodeElement parent = null;
			/*if(parentId != null) {
				parent = getCodeElementById(parentId);
			}*/
			
			SPL spl = genericDao.getSplById(splId);
			
			return new Directory(id, path, type, spl, parent);
		}
		
	}
	
	private class PartRowMapper implements RowMapper<Part>{

		@Override
		public Part mapRow(ResultSet rs, int rowNum) throws SQLException {
			// ID,PATH,TYPE,PARENT,SPL_ID,PART_TYPE
			String id = rs.getString(1);
			String path = rs.getString(2);
			String type = rs.getString(3);
			String parentId = rs.getString(4);
			String splId = rs.getString(5);
			String partType = rs.getString(6);
			
			CodeElement parent = getCodeElementById(parentId);
			SPL spl = genericDao.getSplById(splId);
			
			return new Part(id, path, type, spl, parent, partType);
		}
		
	}
	
	private class CodeFileRowMapper implements RowMapper<CodeFile>{

		@Override
		public CodeFile mapRow(ResultSet rs, int rowNum) throws SQLException {
			// ID,PATH,TYPE,PARENT,SPL_ID,FILENAME
			String id = rs.getString(1);
			String path = rs.getString(2);
			String type = rs.getString(3);
			String parentId = rs.getString(4);
			String splId = rs.getString(5);
			String filename = rs.getString(6);
			
			CodeElement parent = getCodeElementById(parentId);
			SPL spl = genericDao.getSplById(splId);
			
			return new CodeFile(id, path, type, spl, parent, filename);
		}
		
	}

}

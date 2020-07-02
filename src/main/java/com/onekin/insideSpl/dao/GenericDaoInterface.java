package com.onekin.insideSpl.dao;

import java.util.List;

import com.onekin.insideSpl.domain.SPL;

public interface GenericDaoInterface {
	
	public List<SPL> getAllSpl();
	public SPL getSplById(String id);

}

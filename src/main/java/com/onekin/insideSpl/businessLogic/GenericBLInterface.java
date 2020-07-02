package com.onekin.insideSpl.businessLogic;

import java.util.List;

import com.onekin.insideSpl.domain.SPL;

public interface GenericBLInterface {
	
	public List<SPL> getAllSpl();
	public SPL getSplById(String id);

}

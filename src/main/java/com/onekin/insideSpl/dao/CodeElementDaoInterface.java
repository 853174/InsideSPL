package com.onekin.insideSpl.dao;

import java.util.List;

import com.onekin.insideSpl.domain.CodeElement;

public interface CodeElementDaoInterface {
	
	public List<CodeElement> getAllCodeElementsOfSpl(String splId);
	public CodeElement getCodeElementById(String id);

}

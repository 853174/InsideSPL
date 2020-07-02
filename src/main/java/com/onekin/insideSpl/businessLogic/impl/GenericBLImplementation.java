package com.onekin.insideSpl.businessLogic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onekin.insideSpl.businessLogic.GenericBLInterface;
import com.onekin.insideSpl.dao.GenericDaoInterface;
import com.onekin.insideSpl.domain.SPL;

@Service
public class GenericBLImplementation implements GenericBLInterface {

	@Autowired
	private GenericDaoInterface genericDao;
	
	@Override
	public List<SPL> getAllSpl() {
		return genericDao.getAllSpl();
	}

	@Override
	public SPL getSplById(String id) {
		return genericDao.getSplById(id);
	}

}

package com.onekin.insideSpl.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.onekin.insideSpl.domain.VariantModel;

public interface ProductDaoInterface {
	
	public List<VariantModel> getAllProductsOfSpl(String splId);
	public VariantModel getProductById(String id);

}

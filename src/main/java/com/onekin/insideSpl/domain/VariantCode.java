package com.onekin.insideSpl.domain;

public class VariantCode extends VariantComponent{
	
	private CodeElement codeElement;
	
	public VariantCode(String id, boolean isSelected, VariantModel variantModel, CodeElement ce) {
		super(id, isSelected, variantModel);
		this.codeElement = ce;
	}

	public CodeElement getCodeElement() {
		return codeElement;
	}

	public void setCodeElement(CodeElement code) {
		this.codeElement = code;
	}
	
	

}

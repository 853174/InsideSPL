package com.onekin.insideSpl.domain;

public class Code_VariationPoint extends VariationPoint{
	
	// Atributes
	private int startLine;
	private int endLine;
	private String content;
	
	// Relations are given by VariationPoint Class
	
	public Code_VariationPoint(String id, int startLine, int endLine, String expresion, CodeFile file,int vpSize, String content) {
		super(id,expresion,file,vpSize);
		this.startLine = startLine;
		this.endLine = endLine;
		this.content = content;
	}

	public int getStartLine() {
		return startLine;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}

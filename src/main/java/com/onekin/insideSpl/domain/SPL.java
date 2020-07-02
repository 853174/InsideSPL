package com.onekin.insideSpl.domain;

import java.io.File;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SPL {

	// Attributes
	private String id;
	private String name;
	private String lastChange;
	private String gitHubUrl;

	// Relations
	private List<CodeElement> codeElements;
	private List<FeatureModel> featureModels;
	private List<VariantModel> variantModels;

	public SPL(String id, String name, String lastChange, String ghu) {
		this.id = id;
		this.name = name;
		this.lastChange = lastChange;
		this.gitHubUrl = ghu;
		this.codeElements = new ArrayList<>();
		this.featureModels = new ArrayList<>();
		this.variantModels = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FeatureModel> getFeatureModels() {
		return featureModels;
	}
	
	public void addFeatureModel(FeatureModel fm) {
		this.featureModels.add(fm);
	}

	public List<VariantModel> getVariantModels() {
		return variantModels;
	}

	public void addVariantModel(VariantModel vm) {
		this.variantModels.add(vm);
	}

	public List<CodeElement> getCodeElements() {
		return codeElements;
	}

	public void addCodeElement(CodeElement ce) {
		this.codeElements.add(ce);
	}

	public String getLastChange() {
		return lastChange;
	}

	public void setLastChange(String lastChange) {
		this.lastChange = lastChange;
	}

	public String getGitHubUrl() {
		return gitHubUrl;
	}

	public void setGitHubUrl(String gitHubUrl) {
		this.gitHubUrl = gitHubUrl;
	}

	

}

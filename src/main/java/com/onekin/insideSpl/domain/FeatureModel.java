package com.onekin.insideSpl.domain;

import java.util.ArrayList;
import java.util.List;

public class FeatureModel {

	// Attributes
	private String id;
	private String filename;
	private String path;

	// Relations
	private SPL spl;
	private List<Feature> features = new ArrayList<>();
	
	public FeatureModel(String id, String filename, String path, SPL spl) {
		super();
		this.id = id;
		this.filename = filename;
		this.path = path;
		this.spl = spl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public SPL getSpl() {
		return spl;
	}

	public void setSpl(SPL spl) {
		this.spl = spl;
	}
	
	public List<Feature> getFeatures() {
		return this.features;
	}
	
	public void addFeature(Feature f) {
		this.features.add(f);
	}

}

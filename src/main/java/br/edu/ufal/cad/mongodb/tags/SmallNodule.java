package br.edu.ufal.cad.mongodb.tags;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SmallNodule implements Nodule{
	
	//nodulos < 3mm
	private String noduleId;
	private List<Roi> rois;

	@JsonCreator
	public SmallNodule(@JsonProperty("noduleID")String noduleId,
					   @JsonProperty("roi")List<Roi> rois) {
		this.noduleId = noduleId;
		this.rois = rois;
	}

	public SmallNodule(){
		rois = new ArrayList<Roi>();
	}

	public void setNoduleId(String noduleId) {
		this.noduleId = noduleId;
	}

	public void setRois(List<Roi> rois) {
		this.rois = rois;
	}

	@Override
	public String getNoduleID() {
		return noduleId;
	}

	@Override
	public List<Roi> getRois() {
		return rois;
	}

}

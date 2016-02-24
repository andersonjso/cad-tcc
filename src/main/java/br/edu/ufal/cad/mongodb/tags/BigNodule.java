package br.edu.ufal.cad.mongodb.tags;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BigNodule implements Nodule{
	
	//nodulos >= 3mm
	private String noduleId;
	private List<Roi> rois;
	private Characteristics characteristics;
	private int subtlety;
	private int internalStructure;
	private int calcification;
	private int sphericity;
	private int margin;
	private int lobulation;
	private int spiculation;
	private int texture;
	private int malignancy;
	private double[] textureAttributes;

	@JsonCreator
	public BigNodule(@JsonProperty("noduleID") String noduleId,
					 @JsonProperty("roi") List<Roi> rois,
					 @JsonProperty("subtlety") int subtlety,
					 @JsonProperty("internalStructure") int internalStructure,
					 @JsonProperty("calcification") int calcification,
					 @JsonProperty("sphericity") int sphericity,
					 @JsonProperty("margin") int margin,
					 @JsonProperty("lobulation")int lobulation,
					 @JsonProperty("spiculation") int spiculation,
					 @JsonProperty("texture") int texture,
					 @JsonProperty("malignancy") int malignancy,
					 @JsonProperty("textureAttributes") double[] textureAttributes) {
		this.noduleId = noduleId;
		this.rois = rois;
		this.subtlety = subtlety;
		this.internalStructure = internalStructure;
		this.calcification = calcification;
		this.sphericity = sphericity;
		this.margin = margin;
		this.lobulation = lobulation;
		this.spiculation = spiculation;
		this.texture = texture;
		this.malignancy = malignancy;
		this.textureAttributes = textureAttributes;
	}

	public BigNodule(String noduleId, List<Roi> rois){
		this.setRois(rois);		
		this.noduleId = noduleId;
	}

	public Characteristics getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(Characteristics characteristics) {
		this.characteristics = characteristics;
	}

	public void setNoduleId(String noduleId) {
		this.noduleId = noduleId;
	}

	public void setRois(List<Roi> rois) {
		this.rois = rois;
	}

	public int getSubtlety() {
		return subtlety;
	}
	public void setSubtlety(int subtlety) {
		this.subtlety = subtlety;
	}
	public int getInternalStructure() {
		return internalStructure;
	}
	public void setInternalStructure(int internalStructure) {
		this.internalStructure = internalStructure;
	}
	public int getCalcification() {
		return calcification;
	}
	public void setCalcification(int calcification) {
		this.calcification = calcification;
	}
	public int getSphericity() {
		return sphericity;
	}
	public void setSphericity(int sphericity) {
		this.sphericity = sphericity;
	}
	public int getMargin() {
		return margin;
	}
	public void setMargin(int margin) {
		this.margin = margin;
	}
	public int getLobulation() {
		return lobulation;
	}
	public void setLobulation(int lobulation) {
		this.lobulation = lobulation;
	}
	public int getSpiculation() {
		return spiculation;
	}
	public void setSpiculation(int spiculation) {
		this.spiculation = spiculation;
	}
	public int getTexture() {
		return texture;
	}
	public void setTexture(int texture) {
		this.texture = texture;
	}
	public int getMalignancy() {
		return malignancy;
	}
	public void setMalignancy(int malignancy) {
		this.malignancy = malignancy;
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

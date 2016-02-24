package br.edu.ufal.cad.mongodb.tags;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NonNodule {
	
	private String nonNoduleID;
	private String imageZposition;
	private String imageSOP_UID;
	private Locus locus;
	private String xCoord;
	private String yCoord;
	private String originalImage;

	@JsonCreator
	public NonNodule(@JsonProperty("nonNoduleID") String nonNoduleID,
					 @JsonProperty("imageZPosition") String imageZposition,
					 @JsonProperty("imageSOP_UID") String imageSOP_UID,
					 @JsonProperty("originalImage") String originalImage,
					 @JsonProperty("xCoord")String xCoord,
					 @JsonProperty("yCoord")String yCoord) {
		this.nonNoduleID = nonNoduleID;
		this.imageZposition = imageZposition;
		this.imageSOP_UID = imageSOP_UID;
		this.originalImage = originalImage;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}

	public NonNodule(){
		locus = new Locus();
	}

	public String getNonNoduleID() {
		return nonNoduleID;
	}

	public void setNonNoduleID(String nonNoduleID) {
		this.nonNoduleID = nonNoduleID;
	}

	public String getImageZposition() {
		return imageZposition;
	}

	public void setImageZposition(String imageZposition) {
		this.imageZposition = imageZposition;
	}

	public String getImageSOP_UID() {
		return imageSOP_UID;
	}

	public void setImageSOP_UID(String imageSOP_UID) {
		this.imageSOP_UID = imageSOP_UID;
	}

	public Locus getLocus() {
		return locus;
	}

	public void setLocus(Locus locus) {
		this.locus = locus;
	}
	
	

}

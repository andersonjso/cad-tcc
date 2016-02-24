package br.edu.ufal.cad.mongodb.tags;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Roi {
	
	private String imageZposition;	
	private String imageSOP_UID;
	private String originalImage;
	private String inclusion;
	private List<EdgeMap> edgeMaps;
	private String noduleImage;

	@JsonCreator
	public Roi(@JsonProperty("imageZposition")String imageZposition,
			   @JsonProperty("imageSOP_UID")String imageSOP_UID,
			   @JsonProperty("originalImage")String originalImage,
			   @JsonProperty("inclusion")String inclusion,
			   @JsonProperty("edgeMaps")List<EdgeMap> edgeMaps,
			   @JsonProperty("noduleImage")String noduleImage) {
		this.imageZposition = imageZposition;
		this.imageSOP_UID = imageSOP_UID;
		this.originalImage = originalImage;
		this.inclusion = inclusion;
		this.edgeMaps = edgeMaps;
		this.noduleImage = noduleImage;
	}

	public Roi(String imageSOP_UID, String originalImage, List<EdgeMap> edgeMaps){
		this.setImageSOP_UID(imageSOP_UID);
		this.setOriginalImage(originalImage);
		this.setEdgeMaps(edgeMaps);
	}
	
	public String getOriginalImage() {
		return originalImage;
	}

	public void setOriginalImage(String originalImage) {
		this.originalImage = originalImage;
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
	public String getInclusion() {
		return inclusion;
	}
	public void setInclusion(String inclusion) {
		this.inclusion = inclusion;
	}
	public List<EdgeMap> getEdgeMaps() {
		return edgeMaps;
	}
	public void setEdgeMaps(List<EdgeMap> edgeMaps) {
		this.edgeMaps = edgeMaps;
	} 

}

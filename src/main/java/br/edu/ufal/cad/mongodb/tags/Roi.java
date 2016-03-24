package br.edu.ufal.cad.mongodb.tags;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.javafx.beans.IDProperty;
import org.bson.types.ObjectId;

import java.util.List;

public class Roi {
	
	private String imageZposition;	
	private String imageSOP_UID;
	private ObjectId originalImage;
	private String inclusion;
	private List<EdgeMap> edgeMaps;
	private ObjectId noduleImage;

	@JsonCreator
	public Roi(@JsonProperty("imageZposition")String imageZposition,
			   @JsonProperty("imageSOP_UID")String imageSOP_UID,
			   @JsonProperty("originalImage")ObjectId originalImage,
			   @JsonProperty("inclusion")String inclusion,
			   @JsonProperty("edgeMaps")List<EdgeMap> edgeMaps,
			   @JsonProperty("noduleImage")ObjectId noduleImage) {
		this.imageZposition = imageZposition;
		this.imageSOP_UID = imageSOP_UID;
		this.originalImage = originalImage;
		this.inclusion = inclusion;
		this.edgeMaps = edgeMaps;
		this.noduleImage = noduleImage;
	}

	public Roi(String noduleImage) {
		this.noduleImage = new ObjectId(noduleImage);
	}

	public Roi(String imageSOP_UID, ObjectId originalImage, List<EdgeMap> edgeMaps){
		this.setImageSOP_UID(imageSOP_UID);
		this.setOriginalImage(originalImage);
		this.setEdgeMaps(edgeMaps);
	}
	
	public ObjectId getOriginalImage() {
		return originalImage;
	}

	public void setOriginalImage(ObjectId originalImage) {
		this.originalImage = originalImage;
	}

	public void setNoduleImage(ObjectId noduleImage) {
		this.noduleImage = noduleImage;
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

	public ObjectId getNoduleImage() {
		return noduleImage;
	}
}

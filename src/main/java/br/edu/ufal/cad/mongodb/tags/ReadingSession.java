package br.edu.ufal.cad.mongodb.tags;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class ReadingSession {
	
	private String annotationVersion;
	private String servicingRadiologistID;
	private List<BigNodule> bignodule;//nodulos >= 3mm
	private List<SmallNodule> smallnodule;//nodulos < 3mm
	private List<NonNodule> nonnodule;

	@JsonCreator
	public ReadingSession(@JsonProperty("annotationVersion")String annotationVersion,
						  @JsonProperty("servicingRadiologistID")String servicingRadiologistID,
						  @JsonProperty("bignodule") List<BigNodule> bignodule,
						  @JsonProperty("smallnodule")List<SmallNodule> smallnodule,
						  @JsonProperty("nonnodule")List<NonNodule> nonnodule) {
		this.annotationVersion = annotationVersion;
		this.servicingRadiologistID = servicingRadiologistID;
		this.bignodule = bignodule;
		this.smallnodule = smallnodule;
		this.nonnodule = nonnodule;
	}

	public ReadingSession(){
		bignodule = new ArrayList<BigNodule>();
		smallnodule = new ArrayList<SmallNodule>();
		nonnodule = new ArrayList<NonNodule>();
	}

	public String getAnnotationVersion() {
		return annotationVersion;
	}

	public void setAnnotationVersion(String annotationVersion) {
		this.annotationVersion = annotationVersion;
	}

	public String getServicingRadiologistID() {
		return servicingRadiologistID;
	}

	public void setServicingRadiologistID(String servicingRadiologistID) {
		this.servicingRadiologistID = servicingRadiologistID;
	}

	public List<BigNodule> getBignodule() {
		return bignodule;
	}

	public void setBignodule(List<BigNodule> bignodule) {
		this.bignodule = bignodule;
	}

	public List<SmallNodule> getSmallnodule() {
		return smallnodule;
	}

	public void setSmallnodule(List<SmallNodule> smallnodule) {
		this.smallnodule = smallnodule;
	}

	public List<NonNodule> getNonnodule() {
		return nonnodule;
	}

	public void setNonnodule(List<NonNodule> nonnodule) {
		this.nonnodule = nonnodule;
	}

}

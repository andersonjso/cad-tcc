package br.edu.ufal.cad.mongodb.tags;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class ReadingSession {
	
	private String annotationVersion;
	private String servicingRadiologistID;
	private List<BigNodule> bigNodules;//nodulos >= 3mm
	private List<SmallNodule> smallNodules;//nodulos < 3mm
	private List<NonNodule> nonNodules;

	@JsonCreator
	public ReadingSession(@JsonProperty("annotationVersion")String annotationVersion,
						  @JsonProperty("servicingRadiologistID")String servicingRadiologistID,
						  @JsonProperty("bignodule") List<BigNodule> bigNodules,
						  @JsonProperty("smallnodule")List<SmallNodule> smallNodules,
						  @JsonProperty("nonnodule")List<NonNodule> nonNodules) {
		this.annotationVersion = annotationVersion;
		this.servicingRadiologistID = servicingRadiologistID;
		this.bigNodules = bigNodules;
		this.smallNodules = smallNodules;
		this.nonNodules = nonNodules;
	}

	public ReadingSession(){
		bigNodules = new ArrayList<BigNodule>();
		smallNodules = new ArrayList<SmallNodule>();
		nonNodules = new ArrayList<NonNodule>();
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

	public List<BigNodule> getBigNodules() {
		return bigNodules;
	}

	public void setBigNodules(List<BigNodule> bigNodules) {
		this.bigNodules = bigNodules;
	}

	public List<SmallNodule> getSmallNodules() {
		return smallNodules;
	}

	public void setSmallNodules(List<SmallNodule> smallNodules) {
		this.smallNodules = smallNodules;
	}

	public List<NonNodule> getNonNodules() {
		return nonNodules;
	}

	public void setNonNodules(List<NonNodule> nonNodules) {
		this.nonNodules = nonNodules;
	}

}

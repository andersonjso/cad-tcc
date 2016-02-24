package br.edu.ufal.cad.mongodb.tags;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EdgeMap{

	private String xCoord;
	private String yCoord;

	@JsonCreator
	public EdgeMap(@JsonProperty("xCoord") String xCoord,
				   @JsonProperty("yCoord") String yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
}

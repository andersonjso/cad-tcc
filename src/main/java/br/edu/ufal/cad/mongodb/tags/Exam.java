package br.edu.ufal.cad.mongodb.tags;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jongo.marshall.jackson.oid.MongoId;

import java.util.ArrayList;
import java.util.List;


public class Exam {
	
	private String id; //_id gerado pelo mongoDB
	private String path;
	private String uid; //tag LidcReadMessage uid do xml
	private ResponseHeader responseHeader;
	private ReadingSession readingSession;
	private String _id;

	@JsonCreator
	public Exam(@JsonProperty("_id") String _id,
				@JsonProperty("path") String path,
				@JsonProperty("uid") String uid,
				@JsonProperty("ResponseHeader") ResponseHeader responseHeader,
				@JsonProperty("readingSession") ReadingSession readingSession) {
		this._id = _id;
		this.path = path;
		this.uid = uid;
		this.responseHeader = responseHeader;
		this.readingSession = readingSession;
	}

	public Exam(){
		responseHeader = new ResponseHeader();
		readingSession = new ReadingSession();
	}
	
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	public ReadingSession getReadingSession() {
		return readingSession;
	}
	public void setReadingSession(ReadingSession readingSession) {
		this.readingSession = readingSession;
	}
	
	

}

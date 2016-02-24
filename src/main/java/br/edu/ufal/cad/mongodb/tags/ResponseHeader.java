package br.edu.ufal.cad.mongodb.tags;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseHeader {
	
	private String version;
	private String messageId;
	private String dateRequest;
	private String timeRequest;
	private String requestingSite;
	private String servicingSite;
	private String taskDescription;
	private String ctImageFile;
	private String seriesInstanceUid;
	private String dateService;
	private String timeService;
	private String responseDescription;
	private String studyInstanceUID;

	@JsonCreator
	public ResponseHeader(@JsonProperty("Version") String version,
						  @JsonProperty("MessageId") String messageId,
						  @JsonProperty("DateRequest") String dateRequest,
						  @JsonProperty("TimeRequest") String timeRequest,
						  @JsonProperty("RequestingSite") String requestingSite,
						  @JsonProperty("ServicingSite")String servicingSite,
						  @JsonProperty("TaskDescription")String taskDescription,
						  @JsonProperty("CtImageFile") String ctImageFile,
						  @JsonProperty("SeriesInstanceUid")String seriesInstanceUid,
						  @JsonProperty("DateService")String dateService,
						  @JsonProperty("TimeService")String timeService,
						  @JsonProperty("ResponseDescription")String responseDescription,
						  @JsonProperty("StudyInstanceUID")String studyInstanceUID) {
		this.version = version;
		this.messageId = messageId;
		this.dateRequest = dateRequest;
		this.timeRequest = timeRequest;
		this.requestingSite = requestingSite;
		this.servicingSite = servicingSite;
		this.taskDescription = taskDescription;
		this.ctImageFile = ctImageFile;
		this.seriesInstanceUid = seriesInstanceUid;
		this.dateService = dateService;
		this.timeService = timeService;
		this.responseDescription = responseDescription;
		this.studyInstanceUID = studyInstanceUID;
	}

	public ResponseHeader() {
	}

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getDateRequest() {
		return dateRequest;
	}
	public void setDateRequest(String dateRequest) {
		this.dateRequest = dateRequest;
	}
	public String getTimeRequest() {
		return timeRequest;
	}
	public void setTimeRequest(String timeRequest) {
		this.timeRequest = timeRequest;
	}
	public String getRequestingSite() {
		return requestingSite;
	}
	public void setRequestingSite(String requestingSite) {
		this.requestingSite = requestingSite;
	}
	public String getServicingSite() {
		return servicingSite;
	}
	public void setServicingSite(String servicingSite) {
		this.servicingSite = servicingSite;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getCtImageFile() {
		return ctImageFile;
	}
	public void setCtImageFile(String ctImageFile) {
		this.ctImageFile = ctImageFile;
	}
	public String getSeriesInstanceUid() {
		return seriesInstanceUid;
	}
	public void setSeriesInstanceUid(String seriesInstanceUid) {
		this.seriesInstanceUid = seriesInstanceUid;
	}
	public String getDateService() {
		return dateService;
	}
	public void setDateService(String dateService) {
		this.dateService = dateService;
	}
	public String getTimeService() {
		return timeService;
	}
	public void setTimeService(String timeService) {
		this.timeService = timeService;
	}
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	public String getStudyInstanceUID() {
		return studyInstanceUID;
	}
	public void setStudyInstanceUID(String studyInstanceUID) {
		this.studyInstanceUID = studyInstanceUID;
	}

}

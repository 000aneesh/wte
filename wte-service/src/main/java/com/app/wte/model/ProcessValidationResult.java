package com.app.wte.model;

import java.util.Date;

public class ProcessValidationResult {

	String fileName;
	String  efid;
	String clientName;
	String sourcePath;
	String stageName;
	String status;
	int totalRecordsCount;
	int validRecordsCount;
	int invalidRecordsCount;
	Date fileArrivalTimestamp;
	double ruleVersion;
	String isCompressed;
	Date processedTimestamp;
	String processedUserId;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getEfid() {
		return efid;
	}
	public void setEfid(String efid) {
		this.efid = efid;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getStageName() {
		return stageName;
	}
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getTotalRecordsCount() {
		return totalRecordsCount;
	}
	public void setTotalRecordsCount(int totalRecordsCount) {
		this.totalRecordsCount = totalRecordsCount;
	}
	public int getValidRecordsCount() {
		return validRecordsCount;
	}
	public void setValidRecordsCount(int validRecordsCount) {
		this.validRecordsCount = validRecordsCount;
	}
	public int getInvalidRecordsCount() {
		return invalidRecordsCount;
	}
	public void setInvalidRecordsCount(int invalidRecordsCount) {
		this.invalidRecordsCount = invalidRecordsCount;
	}
	public Date getFileArrivalTimestamp() {
		return fileArrivalTimestamp;
	}
	public void setFileArrivalTimestamp(Date fileArrivalTimestamp) {
		this.fileArrivalTimestamp = fileArrivalTimestamp;
	}
	public double getRuleVersion() {
		return ruleVersion;
	}
	public void setRuleVersion(double ruleVersion) {
		this.ruleVersion = ruleVersion;
	}
	public String getIsCompressed() {
		return isCompressed;
	}
	public void setIsCompressed(String isCompressed) {
		this.isCompressed = isCompressed;
	}
	public Date getProcessedTimestamp() {
		return processedTimestamp;
	}
	public void setProcessedTimestamp(Date processedTimestamp) {
		this.processedTimestamp = processedTimestamp;
	}
	public String getProcessedUserId() {
		return processedUserId;
	}
	public void setProcessedUserId(String processedUserId) {
		this.processedUserId = processedUserId;
	}
	
}

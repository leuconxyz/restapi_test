package org.acme.tika;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.acme.serializer.*;

public class OcrDocumentRaw {
    private String sha;
    private String name;
    private String text;
    private Map<String, String> metadata;
    @JsonDeserialize(using = IndiLocalDateTimeDeserializer.class)
    @JsonSerialize(using = IndiLocalDateTimeSerializer.class)
    private LocalDateTime indexDate;
    private String indexAuthor;
    private String uploadedFrom;
    
	public OcrDocumentRaw() {
		super();
	}
    
    public OcrDocumentRaw(String sha, String name, String text, Map<String, String> metadata, LocalDateTime indexDate, String indexAuthor, String uploadedFrom) {
		super();
		this.sha = sha;
		this.name = name;
		this.text = text;
		this.metadata = metadata;
		this.indexDate = indexDate;
		this.indexAuthor = indexAuthor;
		this.uploadedFrom = uploadedFrom;
	}
	
	public OcrDocumentRaw(String sha, String name, String text, Map<String, String> metadata, String indexAuthor, String uploadedFrom) {
		super();
		this.sha = sha;
		this.name = name;
		this.text = text;
		this.metadata = metadata;
		this.indexAuthor = indexAuthor;
		this.uploadedFrom = uploadedFrom;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public LocalDateTime getIndexDate() {
		return indexDate;
	}

	public void setIndexDate(LocalDateTime indexDate) {
		this.indexDate = indexDate;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public String getIndexAuthor() {
		return indexAuthor;
	}

	public void setIndexAuthor(String indexAuthor) {
		this.indexAuthor = indexAuthor;
	}

	public String getUploadedFrom() {
		return uploadedFrom;
	}

	public void setUploadedFrom(String uploadedFrom) {
		this.uploadedFrom = uploadedFrom;
	}
	
	
    
    
}

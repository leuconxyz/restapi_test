package org.acme.tika;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.acme.serializer.*;

public class OcrDocument {
    private BigInteger sha;
    private String name;
    private String text;
    private Map<String, String> metadata;
    @JsonDeserialize(using = IndiLocalDateTimeDeserializer.class)
    @JsonSerialize(using = IndiLocalDateTimeSerializer.class)
    private LocalDateTime indexDate;
    private String indexAuthor;
    private String uploadedFrom;
    
	public OcrDocument() {
		super();
	}
    
    public OcrDocument(BigInteger dhs, String name, String text, Map<String, String> metadata, LocalDateTime indexDate, String indexAuthor, String uploadedFrom) {
		super();
		this.sha = sha;
		this.name = name;
		this.text = text;
		this.metadata = metadata;
		this.indexDate = indexDate;
		this.indexAuthor = indexAuthor;
		this.uploadedFrom = uploadedFrom;
	}
	
	public OcrDocument(BigInteger sha, String name, String text, Map<String, String> metadata, String indexAuthor, String uploadedFrom) {
		super();
		this.sha = sha;
		this.name = name;
		this.text = text;
		this.metadata = metadata;
		this.indexAuthor = indexAuthor;
		this.uploadedFrom = uploadedFrom;
	}

	public BigInteger getSha() {
		return sha;
	}

	public void setSha(BigInteger sha) {
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
	
	public void setIndexDate(String indexDate) {
		this.indexDate = LocalDateTime.parse(indexDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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

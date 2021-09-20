package org.acme.tika;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OcrDocument {
    private BigInteger id;
    private String name;
    private String text;
    private Map<String, String> metadata;
    @JsonIgnore
    private LocalDateTime indexDate;
    private String indexAuthor;
    private String uploadedFrom;
    
    @JsonIgnore
	public OcrDocument() {
		super();
	}
    
    @JsonIgnore
	public OcrDocument(BigInteger id, String name, String text, Map<String, String> metadata, LocalDateTime indexDate, String indexAuthor, String uploadedFrom) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
		this.metadata = metadata;
		this.indexDate = indexDate;
		this.indexAuthor = indexAuthor;
		this.uploadedFrom = uploadedFrom;
	}
	
	public OcrDocument(BigInteger id, String name, String text, Map<String, String> metadata, String indexAuthor, String uploadedFrom) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
		this.metadata = metadata;
		this.indexAuthor = indexAuthor;
		this.uploadedFrom = uploadedFrom;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
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
	
	@JsonIgnore
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

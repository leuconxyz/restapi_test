package org.acme.tika;

public class OcrDocument {
	private OcrDocumentRaw _source;
	private OcrDocumentHighlighted highlight;
	
	public OcrDocument() {
		super();
	}

	public OcrDocument(OcrDocumentRaw _source, OcrDocumentHighlighted highlight) {
		super();
		this._source = _source;
		this.highlight = highlight;
	}

	public OcrDocumentRaw get_source() {
		return _source;
	}

	public void set_source(OcrDocumentRaw _source) {
		this._source = _source;
	}

	public OcrDocumentHighlighted getHighlight() {
		return highlight;
	}

	public void setHighlight(OcrDocumentHighlighted highlight) {
		this.highlight = highlight;
	}
	
	
}

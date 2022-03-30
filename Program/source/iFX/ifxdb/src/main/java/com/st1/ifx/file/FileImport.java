package com.st1.ifx.file;

public class FileImport {
	private String original;
	private String importId;
	private String status;

	public FileImport() {
	}

	public FileImport(String original, String importId, String status) {
		super();
		this.original = original;
		this.importId = importId;
		this.status = status;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getImportId() {
		return importId;
	}

	public void setImportId(String importId) {
		this.importId = importId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "FileImport [original=" + original + ", importId=" + importId + ", status=" + status + "]";
	}

}

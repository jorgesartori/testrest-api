package br.com.resttesteasy.negocio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseDTO {

	private String path;

	private String method;

	private Integer statusCode;

	public ResponseDTO(String path, String method, Integer statusCode) {
		this.path = path;
		this.method = method;
		this.statusCode = statusCode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@JsonProperty("status-code")
	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
}

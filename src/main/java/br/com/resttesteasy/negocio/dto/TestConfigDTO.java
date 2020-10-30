package br.com.resttesteasy.negocio.dto;

import java.util.List;
import java.util.Map;

public class TestConfigDTO {

	private String urlSpec;

	private Integer statusCode;

	private List<String> skipPaths;

	private List<String> onlyPaths;

	private String token;

	private Map<String, String> pathParams;

	private Map<String, String> headers;

	public Map<String, String> getPathParams() {
		return pathParams;
	}

	public void setPathParams(Map<String, String> pathParams) {
		this.pathParams = pathParams;
	}

	public String getUrlSpec() {
		return urlSpec;
	}

	public void setUrlSpec(String urlSpec) {
		this.urlSpec = urlSpec;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getSkipPaths() {
		return skipPaths;
	}

	public void setSkipPaths(List<String> skipPaths) {
		this.skipPaths = skipPaths;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public List<String> getOnlyPaths() {
		return onlyPaths;
	}

	public void setOnlyPaths(List<String> onlyPaths) {
		this.onlyPaths = onlyPaths;
	}

}

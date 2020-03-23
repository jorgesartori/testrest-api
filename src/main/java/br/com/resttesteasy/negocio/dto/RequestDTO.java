package br.com.resttesteasy.negocio.dto;

import java.util.List;

public class RequestDTO {

	private String urlSpec;

	private List<String> whiteListPaths;

	public String getUrlSpec() {
		return urlSpec;
	}

	public void setUrlSpec(String urlSpec) {
		this.urlSpec = urlSpec;
	}

	public List<String> getWhiteListPaths() {
		return whiteListPaths;
	}

	public void setWhiteListPaths(List<String> whiteListPaths) {
		this.whiteListPaths = whiteListPaths;
	}

}

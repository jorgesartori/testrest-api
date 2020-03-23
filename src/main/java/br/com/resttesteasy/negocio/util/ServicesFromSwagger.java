package br.com.resttesteasy.negocio.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Multimap;

import io.restassured.RestAssured;

public class ServicesFromSwagger {
	private Map<String, Map<String, String>> pathsMethodsAndContentType = new HashMap<String, Map<String, String>>();
	private String baseURI;
	private String basePath;
	private JSONObject paths;

	public ServicesFromSwagger(String completeURLSwaggerJson) throws Throwable {
		System.out.println("Iniciando leitura do swagger (" + completeURLSwaggerJson + ")...");
		try {

			String bodyString = new RestTemplate().getForEntity(completeURLSwaggerJson, String.class).getBody();
			JSONObject body = new JSONObject(bodyString);
			JSONObject pathsJsonObject = body.getJSONObject("paths");
			paths = pathsJsonObject;
			Set<String> listaPaths = new HashSet<String>(Arrays.asList(JSONObject.getNames(pathsJsonObject)));
			basePath = body.getString("basePath");
			baseURI = findBaseUri(completeURLSwaggerJson);
			for (Iterator iterator = listaPaths.iterator(); iterator.hasNext();) {
				String path = (String) iterator.next();

				Map<String, String> methodAndContentType = new HashMap<String, String>();
				JSONObject pathJsonObject = pathsJsonObject.getJSONObject(path);
				Set<String> listaMetodos = new HashSet<String>(Arrays.asList(JSONObject.getNames(pathJsonObject)));
				for (Iterator iterator2 = listaMetodos.iterator(); iterator2.hasNext();) {
					String metodo = (String) iterator2.next();
					String contentType = "application/json";
					if ("put".equals(metodo) || "post".equals(metodo)) {
						JSONArray contentTypes = pathJsonObject.getJSONObject(metodo).getJSONArray("consumes");
						if (contentTypes != null) {
							contentType = (String) contentTypes.get(0);
						}
					}
					methodAndContentType.put(metodo, contentType);
				}
				pathsMethodsAndContentType.put(path, methodAndContentType);
			}
		} catch (Exception e) {
			System.out.println("Não foi possível fazer a leitura do swagger,provavelmente por timeout.");
			throw e;

		}
		System.out.println("Leitura concluída.");
	}

	public ServicesFromSwagger() {
		// TODO Auto-generated constructor stub
	}

	private String findBaseUri(String completeURLSwaggerJson) {
		return completeURLSwaggerJson.split(basePath)[0];
	}

	private String pathWithFakePathParams(String path) {
		return path.replaceAll("\\{[^//]*\\}", "1");
	}

	public void requestOneService(String token, String path, String method, int expectedStatusCode, boolean assertion)
			throws Throwable {
		HttpHeaders headers = new HttpHeaders();

		if (token != null) {
			headers.add("Authorization", "Bearer " + token);
		}
		String contentType = pathsMethodsAndContentType.get(path).get(method.toLowerCase());
		headers.add("Content-Type", contentType);
		String body = "{}";
		// if (contentType.equals("multipart/form-data")) {
		// body = "";
		// restAssuredGiven.multiPart("umCampoApenasParaNaoDarProblemaDeEstrutura",
		// "qualquerCoisa");
		// }
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(body, headers);
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(
					baseURI + basePath + pathWithFakePathParams(path), HttpMethod.POST, request, String.class);
			if (assertion ^ (expectedStatusCode == 200)) {
				throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "teste");
			}
		} catch (HttpClientErrorException e) {
			if (assertion ^ (expectedStatusCode == e.getStatusCode().value())) {
				throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "teste");
			}
		}
	}

	public void requestListOfServices(String token, Multimap<String, String> pathsAndMethods, int expectedStatusCode)
			throws Throwable {
		for (Iterator iterator = pathsAndMethods.keySet().iterator(); iterator.hasNext();) {
			String path = (String) iterator.next();
			for (Iterator iterator2 = pathsAndMethods.get(path).iterator(); iterator2.hasNext();) {
				String method = (String) iterator2.next();
				requestOneService(token, path, method, expectedStatusCode, true);
			}

		}
	}

	public void requestAllServices(String token, int expectedStatusCode, List<String> ignorePaths) throws Throwable {

		for (Iterator iterator = pathsMethodsAndContentType.keySet().iterator(); iterator.hasNext();) {
			String path = (String) iterator.next();
			if (ignorePaths != null && ignorePaths.contains(path))
				continue;
			Set<String> methods = pathsMethodsAndContentType.get(path).keySet();
			for (Iterator iterator2 = methods.iterator(); iterator2.hasNext();) {
				String method = (String) iterator2.next();
				requestOneService(token, path, method, expectedStatusCode, true);

			}
		}
	}

	public void requestAllServices(String token, int expectedStatusCode, boolean assertion, List<String> ignorePaths,
			Multimap<String, String> ignoreServices) throws Throwable {

		for (Iterator iterator = pathsMethodsAndContentType.keySet().iterator(); iterator.hasNext();) {
			String path = (String) iterator.next();
			if (ignorePaths.contains(path))
				continue;
			Set<String> methods = pathsMethodsAndContentType.get(path).keySet();
			for (Iterator iterator2 = methods.iterator(); iterator2.hasNext();) {
				String method = (String) iterator2.next();
				if (!ignoreServices.get(path).contains(method)) {
					requestOneService(token, path, method, expectedStatusCode, assertion);
				}
			}
		}
	}

	public String getBaseURI() {
		return baseURI;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public void test401(String server) throws Exception {
		List<String> msgs = new ArrayList<>();
		this.paths.names().forEach(p -> {
			String path = p.toString();
			JSONObject pathObject = paths.getJSONObject(path);
			JSONObject get = pathObject.optJSONObject("get");
			if (get != null) {
				URL url;
				try {
					url = new URL(server + pathWithFakePathParams(path));
					RestAssured.given().log().all().get(url).then().log().all().assertThat().statusCode(401);
				} catch (Throwable e) {
					msgs.add(path + ": " + e.getMessage());
				}
			}
		});
		if (!msgs.isEmpty()) {
			throw new Exception(String.join(";", msgs));
		}
	}

}

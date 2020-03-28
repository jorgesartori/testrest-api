package br.com.resttesteasy.negocio.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import br.com.resttesteasy.negocio.dto.ResponseDTO;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class ServicesFromSwagger {

	public static List<ResponseDTO> testStatusCode(String urlSpec, int statusCodeExpected, String skipPaths) {

		List<ResponseDTO> resultFailedList = new ArrayList<>();
		Set<String> setSkipPaths = new HashSet<>(Arrays.asList(skipPaths.split("\\s*,\\s*")));

		JSONObject swaggerSpec = getSwaggerSpec(urlSpec);
		boolean isVersao3OAS = swaggerSpec.has("openapi");
		JSONObject paths = swaggerSpec.getJSONObject("paths");

		// for each path
		paths.names().forEach(p -> {
			String path = p.toString();
			if (setSkipPaths.contains(path)) {
				return;
			}
			JSONObject pathObject = paths.getJSONObject(path);

			// for each method
			pathObject.keySet().forEach(method -> {
				JSONObject methodObject = pathObject.getJSONObject(method);

				RequestSpecification restassured = configNewReqSpec(methodObject, isVersao3OAS);
				Integer statusCode = restassured.request(method, buildURL(urlSpec, path, swaggerSpec, isVersao3OAS))
						.getStatusCode();

				if (statusCodeExpected != statusCode) {
					resultFailedList.add(new ResponseDTO(path, method, statusCode));
				}
			});

		});
		return resultFailedList;
	}

	private static RequestSpecification configNewReqSpec(JSONObject methodObject, boolean isVersao3OAS) {
		RequestSpecification result = RestAssured.given();
		String contentType = null;

		if (isVersao3OAS) {
			if (methodObject.has("requestBody")) {
				contentType = methodObject.getJSONObject("requestBody").getJSONObject("content").names().getString(0);
				result.contentType(contentType);
			}
		} else {
			if (methodObject.has("consumes")) {
				contentType = methodObject.getJSONArray("consumes").getString(0);
				result.contentType(contentType);
			}
		}
		if ("multipart/form-data".equals(contentType)) {
			result.multiPart("contrlName", "contentBody");
			result.formParam("key", "value");
		}
		return result;
	}

	private static JSONObject getSwaggerSpec(String urlSpec) {
		String body = new RestTemplate().getForEntity(urlSpec, String.class).getBody();
		return new JSONObject(body);
	}

	private static URL buildURL(String urlSpec, String path, JSONObject swaggerSpec, boolean isVersao3OAS) {
		String basePath = null;
		if (isVersao3OAS) {
			basePath = swaggerSpec.getJSONArray("servers").getJSONObject(0).getString("url");
		} else {
			basePath = swaggerSpec.getString("basePath");
		}
		String baseUrl = urlSpec.split(basePath)[0];
		try {
			return new URL(baseUrl + basePath + pathWithFakePathParams(path));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private static String pathWithFakePathParams(String path) {
		return path.replaceAll("\\{[^//]*\\}", "1");
	}

}

package br.com.resttesteasy.negocio.util;

import static io.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import br.com.resttesteasy.negocio.dto.ResponseDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ServicesFromSwagger {

	private static String basePath;

	public static List<ResponseDTO> testStatusCode(String urlSpec, int statusCodeExpected, String skipPaths,
			String token) {

		List<ResponseDTO> resultFailedList = new ArrayList<>();
		List<String> setSkipPaths = Arrays.asList(skipPaths.trim().split("\\s*,\\s*"));

		JSONObject swaggerSpec = getSwaggerSpec(urlSpec);
		boolean isVersao3OAS = swaggerSpec.has("openapi");
		JSONObject paths = swaggerSpec.getJSONObject("paths");
		basePath = getBasePath(swaggerSpec, isVersao3OAS, paths);

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

				RequestSpecification restassured = configNewReqSpec(methodObject, isVersao3OAS, token);
				Integer statusCode = restassured.request(method, buildURL(urlSpec, path, swaggerSpec, isVersao3OAS))
						.getStatusCode();

				if (statusCodeExpected != statusCode) {
					resultFailedList.add(new ResponseDTO(path, method, statusCode));
				}
			});

		});
		return resultFailedList;
	}

	private static String getBasePath(JSONObject swaggerSpec, boolean isVersao3OAS, JSONObject paths) {
		if (isVersao3OAS) {
			if (swaggerSpec.has("servers")) {
				return swaggerSpec.getJSONArray("servers").getJSONObject(0).getString("url");
			} else {
				// em alguns casos, o plugin gerador do arquivo openapi coloca o contexto da aplicacao em cada path, não usando server.
				// nestes casos, o basepath é a primeira parte de qualquer path.
				return "/".concat(paths.names().get(0).toString().split("/")[1]);
			}
		} else {
			return swaggerSpec.getString("basePath");
		}
	}

	private static RequestSpecification configNewReqSpec(JSONObject methodObject, boolean isVersao3OAS, String token) {
		RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

		if (isNotBlank(token)) {
			request.auth().oauth2(token);
		}

		String contentType = "";
		if (isVersao3OAS) {
			if (methodObject.has("requestBody")) {
				contentType = methodObject.getJSONObject("requestBody").getJSONObject("content").names().getString(0);
				request.contentType(contentType);
			}
		} else {
			if (methodObject.has("consumes")) {
				contentType = methodObject.getJSONArray("consumes").getString(0);
				request.contentType(contentType);
			}
		}

		switch (contentType) {
		case APPLICATION_JSON:
			request.body("{}");
			break;
		case MULTIPART_FORM_DATA:
			request.multiPart("contrlName", "contentBody");
			request.formParam("key", "value");
			break;
		default:
			break;
		}

		return request;
	}

	private static JSONObject getSwaggerSpec(String urlSpec) {
		String body = RestAssured.given().accept(JSON).relaxedHTTPSValidation().baseUri(urlSpec).get().getBody()
				.asString();
		return new JSONObject(body);
	}

	private static URL buildURL(String urlSpec, String path, JSONObject swaggerSpec, boolean isVersao3OAS) {
		String baseUrl = urlSpec.split(basePath)[0];
		try {
			String url = "";
			if (path.contains(basePath)) {
				 url = baseUrl + pathWithFakePathParams(path);
			} else {
				url = baseUrl + basePath + pathWithFakePathParams(path);
			}
			return new URL(url);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private static String pathWithFakePathParams(String path) {
		return path.replaceAll("\\{[^//]*\\}", "1");
	}

}

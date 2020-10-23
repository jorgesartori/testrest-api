package br.com.resttesteasy.negocio.util;

import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.resttesteasy.negocio.dto.ResponseDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ServicesFromSwagger {

	private static String host;

	private static String basePath;

	private static final Logger log = Logger.getLogger(ServicesFromSwagger.class.getName());

	public static List<ResponseDTO> testStatusCode(String urlSpec, int statusCodeExpected, String skipPaths,
			String token) {

		List<ResponseDTO> resultFailedList = new ArrayList<>();
		List<String> setSkipPaths = Arrays.asList(skipPaths.trim().split("\\s*,\\s*"));

		log.info("recuperando openapi.json spec...");
		JSONObject swaggerSpec = getSwaggerSpec(urlSpec);
		log.info("openapi.json spec recuperada com sucesso.");

		boolean isVersao3OAS = swaggerSpec.has("openapi");

		JSONObject paths = swaggerSpec.getJSONObject("paths");
		getBasePath(swaggerSpec, isVersao3OAS, paths);
		host = urlSpec.split(basePath + "/")[0];
		log.info("host configurado: " + host);
		log.info("basePath configurado: " + basePath);

		log.info(format("chamando todos os serviços (esperando status code = %d)...", statusCodeExpected));
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
				Response response = restassured.request(method, buildURL(path));
				response.then().log().status();
				log.info("\n");
				Integer statusCode = response.getStatusCode();

				if (statusCodeExpected != statusCode) {
					resultFailedList.add(new ResponseDTO(path, method, statusCode));
				}
			});

		});

		return resultFailedList;
	}

	private static void getBasePath(JSONObject swaggerSpec, boolean isVersao3OAS, JSONObject paths) {
		if (isVersao3OAS) {
			if (swaggerSpec.has("servers")) {
				basePath = swaggerSpec.getJSONArray("servers").getJSONObject(0).getString("url");
			} else {
				log.info("basePath não informado em 'servers.url', utilizando primeira parte do path.");
				basePath = "/".concat(paths.names().get(0).toString().split("/")[1]);
			}
		} else {
			basePath = swaggerSpec.getString("basePath");
		}
	}

	private static RequestSpecification configNewReqSpec(JSONObject methodObject, boolean isVersao3OAS, String token) {
		RequestSpecification request = RestAssured.given().relaxedHTTPSValidation().log().method().log().uri();

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
		Response response = RestAssured.given().accept(JSON).relaxedHTTPSValidation().baseUri(urlSpec).log().all()
				.get();
		response.then().log().all();
		String body = response.getBody().asString();
		return new JSONObject(body);
	}

	private static URL buildURL(String path) {
		try {
			String url = "";
			if (path.contains(basePath)) {
				url = host + pathWithFakePathParams(path);
			} else {
				url = host + basePath + pathWithFakePathParams(path);
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

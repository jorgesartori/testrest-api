package br.com.resttesteasy.negocio.util;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import io.restassured.RestAssured;

public class ServicesFromSwagger {

	public static List<String> testUnauthorized(String urlSpec) {
		String bodyString = new RestTemplate().getForEntity(urlSpec, String.class).getBody();
		JSONObject swaggerSpec = new JSONObject(bodyString);
		JSONObject paths = swaggerSpec.getJSONObject("paths");
		List<String> msgs = new ArrayList<>();
		paths.names().forEach(p -> {
			String path = p.toString();
			JSONObject pathObject = paths.getJSONObject(path);
			JSONObject get = pathObject.optJSONObject("get");
			if (get != null) {
				int status = RestAssured.given().get(montarURL(urlSpec, path, swaggerSpec)).getStatusCode();
				if (UNAUTHORIZED.value() != status) {
					msgs.add(path + ": GET");
				}
			}
		});
		return msgs;
	}

	private static URL montarURL(String urlSpec, String path, JSONObject swaggerSpec) {
		String basePath = swaggerSpec.getString("basePath");
		String server = urlSpec.split(basePath)[0];
		try {
			return new URL(server + basePath + pathWithFakePathParams(path));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private static String pathWithFakePathParams(String path) {
		return path.replaceAll("\\{[^//]*\\}", "1");
	}

}

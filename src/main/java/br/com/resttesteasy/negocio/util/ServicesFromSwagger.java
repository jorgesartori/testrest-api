package br.com.resttesteasy.negocio.util;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import br.com.resttesteasy.negocio.dto.ResponseDTO;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class ServicesFromSwagger {

    public static List<ResponseDTO> testStatusCode(String urlSpec, int statusCodeExpected, String skipPaths,
        String token) {

        List<ResponseDTO> resultFailedList = new ArrayList<>();
        List<String> setSkipPaths = Arrays.asList(skipPaths.split("\\s*,\\s*"));

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

                RequestSpecification restassured = configNewReqSpec(methodObject, isVersao3OAS, token);
                Integer statusCode = restassured
                    .request(method, buildURL(urlSpec, path, swaggerSpec, isVersao3OAS)).getStatusCode();

                if (statusCodeExpected != statusCode) {
                    resultFailedList.add(new ResponseDTO(path, method, statusCode));
                }
            });

        });
        return resultFailedList;
    }

    private static RequestSpecification configNewReqSpec(JSONObject methodObject, boolean isVersao3OAS,
        String token) {
        RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

        if (isNotBlank(token)) {
            request.auth().oauth2(token);
        }

        String contentType = "";
        if (isVersao3OAS) {
            if (methodObject.has("requestBody")) {
                contentType =
                    methodObject.getJSONObject("requestBody").getJSONObject("content").names().getString(0);
                request.contentType(contentType);
            }
        } else {
            if (methodObject.has("consumes")) {
                contentType = methodObject.getJSONArray("consumes").getString(0);
                request.contentType(contentType);
            }
        }

        switch (contentType) {
        case APPLICATION_JSON_VALUE:
            request.body("{}");
            break;
        case MULTIPART_FORM_DATA_VALUE:
            request.multiPart("contrlName", "contentBody");
            request.formParam("key", "value");
            break;
        default:
            break;
        }

        return request;
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

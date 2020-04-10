package br.com.resttesteasy.negocio.bc;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.List;

import br.com.resttesteasy.negocio.dto.ResponseDTO;
import br.com.resttesteasy.negocio.util.ServicesFromSwagger;

public class SwaggerSpecBCImpl implements SwaggerSpecBC {
	@Override
	public List<ResponseDTO> testStatusCode(String url, int statusCode, String skipPaths, String token) {
		return ServicesFromSwagger.testStatusCode(defaultString(url), statusCode, defaultString(skipPaths), token);
	}

}

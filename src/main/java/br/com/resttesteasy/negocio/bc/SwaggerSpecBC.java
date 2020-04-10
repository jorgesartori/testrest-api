package br.com.resttesteasy.negocio.bc;

import java.util.List;

import br.com.resttesteasy.negocio.dto.ResponseDTO;

public interface SwaggerSpecBC {

	List<ResponseDTO> testStatusCode(String url, int statusCode, String skipPaths, String token);

}

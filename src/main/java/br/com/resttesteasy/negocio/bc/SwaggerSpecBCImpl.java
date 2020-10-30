package br.com.resttesteasy.negocio.bc;

import java.util.List;

import br.com.resttesteasy.negocio.dto.ResponseDTO;
import br.com.resttesteasy.negocio.dto.TestConfigDTO;
import br.com.resttesteasy.negocio.util.ServicesFromSwagger;

public class SwaggerSpecBCImpl implements SwaggerSpecBC {

	@Override
	public List<ResponseDTO> testStatusCode(TestConfigDTO dto) {
		return ServicesFromSwagger.testStatusCode(dto);
	}

}

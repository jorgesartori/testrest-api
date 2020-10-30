package br.com.resttesteasy.negocio.bc;

import java.util.List;

import br.com.resttesteasy.negocio.dto.ResponseDTO;
import br.com.resttesteasy.negocio.dto.TestConfigDTO;

public interface SwaggerSpecBC {

	List<ResponseDTO> testStatusCode(TestConfigDTO dto);

}

package br.com.resttesteasy.rest.api.v1;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.resttesteasy.negocio.bc.SwaggerSpecBC;
import br.com.resttesteasy.negocio.dto.ResponseDTO;
import br.com.resttesteasy.negocio.dto.TestConfigDTO;

@Path("v1/swagger")
public class SwaggerSpecREST {

	@Inject
	private SwaggerSpecBC swaggerBC;

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response testStatusCode(TestConfigDTO dto) {
		List<ResponseDTO> listaFalhas = swaggerBC.testStatusCode(dto);
		if (listaFalhas.isEmpty()) {
			return Response.ok().build();
		}
		return Response.status(422).entity(listaFalhas).build();

	}
}

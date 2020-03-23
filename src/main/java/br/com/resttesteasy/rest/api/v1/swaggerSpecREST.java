package br.com.resttesteasy.rest.api.v1;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.demoiselle.jee.security.annotation.Authenticated;

import br.com.resttesteasy.negocio.bc.SwaggerSpecBC;
import br.com.resttesteasy.negocio.dto.RequestDTO;
import io.swagger.annotations.Api;

@Path("v1/swagger")
@Produces(APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api
public class swaggerSpecREST {

	@Inject
	private SwaggerSpecBC swaggerBC;

	@POST
	public Response testNoAuthNoToken(RequestDTO dto) {
		try {
			swaggerBC.testarTodosServicos(dto.getUrlSpec());
			return Response.status(200).build();
		} catch (Throwable e) {
			return Response.status(422).entity(e.getMessage()).build();
		}

	}

	@GET
	@Authenticated
	public String testaAplicacao() {
		return "jorge";
	}

}

package br.com.resttesteasy.rest.api.v1;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.demoiselle.jee.security.annotation.Authenticated;

import br.com.resttesteasy.negocio.bc.SwaggerSpecBC;
import io.swagger.annotations.Api;

@Path("v1/swagger")
@Produces(APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api
public class swaggerSpecREST {

	@Inject
	private SwaggerSpecBC swaggerBC;

	@GET
	public Response testUnauthorized(@QueryParam(value = "urlSpec") String url) {
		List<String> listaFalhas = swaggerBC.testUnauthorized(url);
		if (listaFalhas.isEmpty()) {
			return Response.ok().build();
		}
		return Response.status(422).entity(listaFalhas).build();

	}

	@Path("logado")
	@GET
	@Authenticated
	public String testaAplicacao() {
		return "jorge";
	}

}

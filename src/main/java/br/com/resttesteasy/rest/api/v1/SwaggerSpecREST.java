package br.com.resttesteasy.rest.api.v1;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.resttesteasy.negocio.bc.SwaggerSpecBC;
import br.com.resttesteasy.negocio.dto.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.jaxrs.PATCH;

@Path("v1/swagger")
@Api
public class swaggerSpecREST {

	@Inject
	private SwaggerSpecBC swaggerBC;

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response testStatusCode(@QueryParam(value = "url-spec") String url,
			@QueryParam(value = "status-code") int statusCode, @QueryParam(value = "skip-paths") String skipPaths) {
		List<ResponseDTO> listaFalhas = swaggerBC.testStatusCode(url, statusCode, skipPaths);
		if (listaFalhas.isEmpty()) {
			return Response.ok().build();
		}
		return Response.status(422).entity(listaFalhas).build();

	}

	// only for self swagger spec test

	@Path("get/unauthorized")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUnauthorized() {
		return Response.status(UNAUTHORIZED).build();
	}

	@Path("get/forbidden")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getForbidden() {
		return Response.status(FORBIDDEN).build();
	}

	@Path("post/ok")
	@POST
	@Produces(APPLICATION_JSON)
	public Response postOk() {
		return Response.ok().build();
	}

	@Path("post/unauthorized")
	@POST
	@Produces(APPLICATION_JSON)
	public Response postUnauthorized() {
		return Response.status(UNAUTHORIZED).build();
	}

	@Path("post/forbidden")
	@POST
	@Produces(APPLICATION_JSON)
	public Response postForbidden() {
		return Response.status(FORBIDDEN).build();
	}

	@Path("put/ok")
	@PUT
	@Produces(APPLICATION_JSON)
	public Response putOk() {
		return Response.ok().build();
	}

	@Path("put/unauthorized")
	@PUT
	@Produces(APPLICATION_JSON)
	public Response putUnauthorized() {
		return Response.status(UNAUTHORIZED).build();
	}

	@Path("put/forbidden")
	@PUT
	@Produces(APPLICATION_JSON)
	public Response putForbidden() {
		return Response.status(FORBIDDEN).build();
	}

	@Path("patch/ok")
	@PATCH
	@Produces(APPLICATION_JSON)
	public Response patchOk() {
		return Response.ok().build();
	}

	@Path("patch/unauthorized")
	@PATCH
	@Produces(APPLICATION_JSON)
	public Response patchUnauthorized() {
		return Response.status(UNAUTHORIZED).build();
	}

	@Path("patch/forbidden")
	@PATCH
	@Produces(APPLICATION_JSON)
	public Response patchForbidden() {
		return Response.status(FORBIDDEN).build();
	}

	@Path("delete/ok")
	@DELETE
	@Produces(APPLICATION_JSON)
	public Response deleteOk() {
		return Response.ok().build();
	}

	@Path("delete/unauthorized")
	@DELETE
	@Produces(APPLICATION_JSON)
	public Response deleteUnauthorized() {
		return Response.status(UNAUTHORIZED).build();
	}

	@Path("delete/forbidden")
	@DELETE
	@Produces(APPLICATION_JSON)
	public Response deleteForbidden() {
		return Response.status(FORBIDDEN).build();
	}

}

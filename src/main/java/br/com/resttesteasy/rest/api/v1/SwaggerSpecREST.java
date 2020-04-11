package br.com.resttesteasy.rest.api.v1;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import br.com.resttesteasy.negocio.bc.SwaggerSpecBC;
import br.com.resttesteasy.negocio.dto.ResponseDTO;

@Path("v1/swagger")
public class SwaggerSpecREST {

    @Inject
    private SwaggerSpecBC swaggerBC;

    @GET
    @Produces(APPLICATION_JSON)
    public Response testStatusCode(@QueryParam(value = "url-spec") String url,
        @QueryParam(value = "status-code") int statusCode, @QueryParam(value = "skip-paths") String skipPaths,
        @QueryParam(value = "token") String token) {
        List<ResponseDTO> listaFalhas = swaggerBC.testStatusCode(url, statusCode, skipPaths, token);
        if (listaFalhas.isEmpty()) {
            return Response.ok().build();
        }
        return Response.status(422).entity(listaFalhas).build();

    }
}

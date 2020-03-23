package br.com.resttesteasy.negocio.bc;

import java.util.List;

import br.com.resttesteasy.negocio.util.ServicesFromSwagger;

public class SwaggerSpecBCImpl implements SwaggerSpecBC {
	@Override
	public List<String> testUnauthorized(String url) {
		return ServicesFromSwagger.testUnauthorized(url);
	}
}

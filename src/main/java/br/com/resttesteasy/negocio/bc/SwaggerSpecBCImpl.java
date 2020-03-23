package br.com.resttesteasy.negocio.bc;

import br.com.resttesteasy.negocio.util.ServicesFromSwagger;

public class SwaggerSpecBCImpl implements SwaggerSpecBC {
	@Override
	public void testarTodosServicos(String url) throws Throwable {
		ServicesFromSwagger servicesFromSwagger = new ServicesFromSwagger(url);
		servicesFromSwagger.test401(url);
	}
}

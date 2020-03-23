package br.com.resttesteasy.rest.api;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.swagger.jaxrs.config.BeanConfig;

@ApplicationPath("api")
public class AppREST extends Application {
	public AppREST() {
		BeanConfig conf = new BeanConfig();
		conf.setTitle("teste de autenticação");
		conf.setDescription("Testa a autenticação de serviços");
		conf.setVersion("1.0.0");
		conf.setHost("localhost:8080");
		conf.setBasePath("/resttesteasy-rest-0.0.1-SNAPSHOT/api");
		conf.setSchemes(new String[] { "http" });
		conf.setResourcePackage("br.com.resttesteasy.rest.api");
		conf.setScan(true);
	}
}

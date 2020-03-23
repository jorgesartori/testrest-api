
package br.com.resttesteasy.negocio.model;

import java.util.List;

public class Get {

    private String operationId;
    private List<String> consumes = null;
    private List<String> produces = null;
    private List<Object> parameters = null;
    private Responses responses;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public Responses getResponses() {
        return responses;
    }

    public void setResponses(Responses responses) {
        this.responses = responses;
    }

}

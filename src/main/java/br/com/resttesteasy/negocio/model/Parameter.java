
package br.com.resttesteasy.negocio.model;


public class Parameter {

    private String in;
    private String name;
    private Boolean required;
    private Schema_ schema;

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Schema_ getSchema() {
        return schema;
    }

    public void setSchema(Schema_ schema) {
        this.schema = schema;
    }

}

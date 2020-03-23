
package br.com.resttesteasy.negocio.model;


public class Properties {

    private UrlSpec urlSpec;
    private WhiteListPaths whiteListPaths;

    public UrlSpec getUrlSpec() {
        return urlSpec;
    }

    public void setUrlSpec(UrlSpec urlSpec) {
        this.urlSpec = urlSpec;
    }

    public WhiteListPaths getWhiteListPaths() {
        return whiteListPaths;
    }

    public void setWhiteListPaths(WhiteListPaths whiteListPaths) {
        this.whiteListPaths = whiteListPaths;
    }

}

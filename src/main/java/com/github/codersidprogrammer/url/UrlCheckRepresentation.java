package com.github.codersidprogrammer.url;

import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;

public class UrlCheckRepresentation extends AbstractPolicyRepresentation {

    private String urlSource;
    private String params;

    public String getUrlSource() {
        return urlSource;
    }

    public void setUrlSource(String urlSource) {
        this.urlSource = urlSource;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }


    public UrlCheckRepresentation(String urlSource, String params) {
        this.urlSource = urlSource;
        this.params = params;
    }

    public UrlCheckRepresentation() {}


}

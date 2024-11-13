package com.github.codersidprogrammer.abac;

import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;

public class AbacPolicyRepresentation extends AbstractPolicyRepresentation {
    private String code;

    public AbacPolicyRepresentation() {
    }

    public String getType() {
        return super.getType() == null ? "js" : super.getType();
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
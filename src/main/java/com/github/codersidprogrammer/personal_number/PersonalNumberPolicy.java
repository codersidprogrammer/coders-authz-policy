package com.github.codersidprogrammer.personal_number;

import org.keycloak.authorization.model.Policy;
import org.keycloak.authorization.model.Resource;
import org.keycloak.authorization.policy.evaluation.Evaluation;
import org.keycloak.authorization.policy.provider.PolicyProvider;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;

public class PersonalNumberPolicy implements PolicyProvider {
    private final KeycloakSession session;

    public PersonalNumberPolicy(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void evaluate(Evaluation evaluation) {
        Policy policy = evaluation.getPolicy();
        Resource resource = evaluation.getPermission().getResource();
        UserModel user = session.getContext().getAuthenticationSession().getAuthenticatedUser();

        HttpRequest httpRequest = session.getContext().getHttpRequest();

        // Example of an attribute check: assuming a `department` attribute
        String requiredDepartment = resource.getAttribute("preferred_username").toString();
        String userDepartment = user.getFirstAttribute("preferred_username");

        if (requiredDepartment != null && requiredDepartment.equals(userDepartment)) {
            evaluation.grant();
        }
    }

    @Override
    public void close() {
    }
}

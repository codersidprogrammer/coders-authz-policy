package com.github.codersidprogrammer.url;

import org.keycloak.authorization.AuthorizationProvider;
import org.keycloak.authorization.model.Policy;
import org.keycloak.authorization.policy.evaluation.Evaluation;
import org.keycloak.authorization.policy.provider.PolicyProvider;
import org.keycloak.models.KeycloakSession;

import java.util.function.BiFunction;
import java.util.logging.Logger;

public class UrlCheckProvider implements PolicyProvider {
    private static final Logger logger = Logger.getLogger(UrlCheckProvider.class.getName());
    private final BiFunction<Policy, AuthorizationProvider, UrlCheckRepresentation> representationFunction;

    public UrlCheckProvider(BiFunction<Policy, AuthorizationProvider, UrlCheckRepresentation> representationFunction) {
        this.representationFunction = representationFunction;
    }
    @Override
    public void evaluate(Evaluation evaluation) {
        KeycloakSession session = evaluation.getAuthorizationProvider().getKeycloakSession();
        String urlSource = session.getContext().getRequestHeaders().getRequestHeader("X-Request-Uri").get(0);
        evaluation.grant();
    }

    @Override
    public void close() {

    }
}

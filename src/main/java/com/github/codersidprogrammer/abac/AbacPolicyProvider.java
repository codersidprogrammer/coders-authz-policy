package com.github.codersidprogrammer.abac;

import org.jboss.logging.Logger;
import org.keycloak.authorization.AuthorizationProvider;
import org.keycloak.authorization.model.Policy;
import org.keycloak.authorization.model.Resource;
import org.keycloak.authorization.policy.evaluation.Evaluation;
import org.keycloak.authorization.policy.provider.PolicyProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakUriInfo;
import org.keycloak.scripting.EvaluatableScriptAdapter;

import javax.script.SimpleScriptContext;
import java.net.URI;
import java.util.function.BiFunction;

public class AbacPolicyProvider implements PolicyProvider {
    private static final Logger logger = Logger.getLogger(AbacPolicyProvider.class);
    private final BiFunction<AuthorizationProvider, Policy, EvaluatableScriptAdapter> evaluatableScript;

    AbacPolicyProvider(BiFunction<AuthorizationProvider, Policy, EvaluatableScriptAdapter> evaluatableScript) {
        this.evaluatableScript = evaluatableScript;
    }

    public void evaluate(Evaluation evaluation) {
        Policy policy = evaluation.getPolicy();
        AuthorizationProvider authorization = evaluation.getAuthorizationProvider();
        EvaluatableScriptAdapter adapter = evaluatableScript.apply(authorization, policy);

//        URI request = authorization.getKeycloakSession().getContext().getHttpRequest().getUri().getRequestUri();
        KeycloakUriInfo request = authorization.getKeycloakSession().getContext().getUri();
        Resource resource = evaluation.getPermission().getResource();
        KeycloakSession session = authorization.getKeycloakSession();
        try {
            SimpleScriptContext context = new SimpleScriptContext();
            context.setAttribute("$evaluation", evaluation, 100);
            context.setAttribute("$request", request, 100);
            context.setAttribute("$resource", resource, 100);
            context.setAttribute("$session", session, 100);
            adapter.eval(context);

//            session.getAttributes();
//            resource.getAttributes();
//            request.getPath();
//            request.getAbsolutePath().getPath();
//            request.getBaseUri().getPath();
//            request.getPath();
//            resource.getUris()
//            session.getContext().getUri().getAbsolutePath().getPath()

            logger.debugf("JS Policy %s evaluated to status %s", policy.getName(), evaluation.getContext());
        } catch (Exception var6) {
            Exception e = var6;
            throw new RuntimeException("Error evaluating JS Policy [" + policy.getName() + "].", e);
        }
    }

    public void close() {
    }
}

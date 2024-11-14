package com.github.codersidprogrammer.abac;

import org.keycloak.Config;
import org.keycloak.authorization.AuthorizationProvider;
import org.keycloak.authorization.model.Policy;
import org.keycloak.authorization.policy.provider.PolicyProvider;
import org.keycloak.authorization.policy.provider.PolicyProviderFactory;
import org.keycloak.authorization.policy.provider.js.ScriptCache;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.models.ScriptModel;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.keycloak.scripting.EvaluatableScriptAdapter;
import org.keycloak.scripting.ScriptingProvider;

public class AbacPolicyFactory implements PolicyProviderFactory<AbacPolicyRepresentation> {
    private static final String POLICY_NAME = "Coders ABAC Policy";
    private static final String POLICY_ID = "coders-abac-policy";
    private ScriptCache scriptCache;
    private final AbacPolicyProvider provider = new AbacPolicyProvider(this::getEvaluatableScript);

    @Override
    public String getName() {
        return POLICY_NAME;
    }

    @Override
    public String getId() {
        return POLICY_ID;
    }

    @Override
    public String getGroup() {
        return "Attribute Rules Based";
    }

    @Override
    public PolicyProvider create(AuthorizationProvider authorizationProvider) {
        return this.provider;
    }

    @Override
    public AbacPolicyRepresentation toRepresentation(Policy policy, AuthorizationProvider authorizationProvider) {
        AbacPolicyRepresentation representation = new AbacPolicyRepresentation();
        representation.setCode((String)policy.getConfig().get("code"));
        return representation;
    }

    @Override
    public Class<AbacPolicyRepresentation> getRepresentationType() {
        return AbacPolicyRepresentation.class;
    }

    @Override
    public PolicyProvider create(KeycloakSession keycloakSession) {
        return null;
    }

    @Override
    public void init(Config.Scope config) {
        int maxEntries = Integer.parseInt(config.get("cache-max-entries", "100"));
        int maxAge = Integer.parseInt(config.get("cache-entry-max-age", "-1"));
        this.scriptCache = new ScriptCache(maxEntries, (long)maxAge);
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isInternal() {
        return false;
    }


    @Override
    public void onUpdate(Policy policy, AbacPolicyRepresentation representation, AuthorizationProvider authorization) {
        policy.setDecisionStrategy(representation.getDecisionStrategy());
        policy.setDescription(policy.getDescription());
        policy.setLogic(policy.getLogic());
    }

    @Override
    public void onImport(Policy policy, PolicyRepresentation representation, AuthorizationProvider authorization) {
        PolicyProviderFactory.super.onImport(policy, representation, authorization);
    }

    @Override
    public void onRemove(Policy policy, AuthorizationProvider authorization) {
        this.scriptCache.remove(policy.getId());
    }

    private EvaluatableScriptAdapter getEvaluatableScript(AuthorizationProvider authz, Policy policy) {
        return this.scriptCache.computeIfAbsent(policy.getId(), (id) -> {
            ScriptingProvider scripting = (ScriptingProvider)authz.getKeycloakSession().getProvider(ScriptingProvider.class);
            ScriptModel script = this.getScriptModel(policy, authz.getRealm(), scripting);
            return scripting.prepareEvaluatableScript(script);
        });
    }

    protected ScriptModel getScriptModel(Policy policy, RealmModel realm, ScriptingProvider scripting) {
        String _code =  "" +
                "print(\"-------\");" +
                "print($evaluation);" +
                "print($request.getAbsolutePath().getPath());" +
                "print($request.getBaseUri().getPath());" +
                "print($request.getPath());" +
                "print($resource.getAttributes());" +
                "print($resource.getUris());" +
                "print($session.getContext().getUri().getAbsolutePath().getPath());" +
                "print($session.getContext().getRequestHeaders().getRequestHeaders());" +
                "$evaluation.grant();";

        String scriptName = policy.getName();
        String scriptCode = _code;//(String) policy.getConfig().get("code");
        String scriptDescription = policy.getDescription();
        return scripting.createScript(realm.getId(), "text/javascript", scriptName, scriptCode, scriptDescription);
    }
}

package com.github.codersidprogrammer.url;

import org.keycloak.Config;
import org.keycloak.authorization.AuthorizationProvider;
import org.keycloak.authorization.model.Policy;
import org.keycloak.authorization.policy.provider.PolicyProvider;
import org.keycloak.authorization.policy.provider.PolicyProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlCheckFactory implements PolicyProviderFactory<UrlCheckRepresentation> {
    private static final String POLICY_NAME = "Coders ABAC URL Check Policy";
    private static final String POLICY_ID = "coders-abac-url-check-policy";

    private UrlCheckProvider provider = new UrlCheckProvider(this::toRepresentation);
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
    public UrlCheckRepresentation toRepresentation(Policy policy, AuthorizationProvider authorizationProvider) {
        UrlCheckRepresentation representation = new UrlCheckRepresentation();
        Map<String, String> config = policy.getConfig();

        representation.setParams(config.get("params"));
        representation.setUrlSource(config.get("urlsource"));

        return representation;
    }

    @Override
    public Class<UrlCheckRepresentation> getRepresentationType() {
        return UrlCheckRepresentation.class;
    }

    @Override
    public PolicyProvider create(KeycloakSession keycloakSession) {
        return this.provider;
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public void onCreate(Policy policy, UrlCheckRepresentation representation, AuthorizationProvider authorization) {
        updatePolicy(policy, representation);
    }

    @Override
    public void onUpdate(Policy policy, UrlCheckRepresentation representation, AuthorizationProvider authorization) {
        updatePolicy(policy, representation);
    }

    @Override
    public void onImport(Policy policy, PolicyRepresentation representation, AuthorizationProvider authorization) {
        policy.setConfig(representation.getConfig());
    }

    private void updatePolicy(Policy policy, UrlCheckRepresentation representation) {
        Map<String, String> config = new HashMap<>(policy.getConfig());

        config.put("params", representation.getParams());
        config.put("urlsource", representation.getUrlSource());

        policy.setConfig(config);
    }




    @Override
    public List<ProviderConfigProperty> getConfigMetadata() {
        return ProviderConfigurationBuilder.create()
                // PARAMS
                .property()
                .name("params")
                .label("Params Name")
                .helpText("Params that exist on URI. e.g `/sample/{personalNumber}/records` , params will be `personalNumber`.")
                .type(ProviderConfigProperty.STRING_TYPE)
                .defaultValue("")
                .add()


                // BUILD
                .build();

    }
}

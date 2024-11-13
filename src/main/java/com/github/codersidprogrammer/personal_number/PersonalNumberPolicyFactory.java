package com.github.codersidprogrammer.personal_number;

import org.keycloak.Config;
import org.keycloak.authorization.AuthorizationProvider;
import org.keycloak.authorization.model.Policy;
import org.keycloak.authorization.policy.provider.PolicyProvider;
import org.keycloak.authorization.policy.provider.PolicyProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class PersonalNumberPolicyFactory implements PolicyProviderFactory<PersonalNumberPolicyRepresentation> {

    private static final String POLICY_NAME = "Attribute-Based Access Control Policy";

    @Override
    public String getId() {
        return "personal-number-policy";
    }

    @Override
    public String getName() {
        return POLICY_NAME;
    }

    @Override
    public String getGroup() {
        return "Attribute Based";
    }

    @Override
    public PolicyProvider create(AuthorizationProvider authorizationProvider) {
        return new PersonalNumberPolicy(authorizationProvider.getKeycloakSession());
    }

    @Override
    public PersonalNumberPolicyRepresentation toRepresentation(Policy policy, AuthorizationProvider authorizationProvider) {
        return new PersonalNumberPolicyRepresentation();
    }

    @Override
    public Class<PersonalNumberPolicyRepresentation> getRepresentationType() {
        return PersonalNumberPolicyRepresentation.class;
    }

    @Override
    public PolicyProvider create(KeycloakSession session) {
        return new PersonalNumberPolicy(session);
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }



    public List<ProviderConfigProperty> getConfigProperties() {
        List<ProviderConfigProperty> configProperties = new ArrayList<>();

        // Define any configuration settings for the ABAC policy here
        ProviderConfigProperty departmentAttribute = new ProviderConfigProperty();
        departmentAttribute.setName("attribute");
        departmentAttribute.setLabel("User Attribute");
        departmentAttribute.setType(ProviderConfigProperty.STRING_TYPE);
        departmentAttribute.setDefaultValue("department");
        departmentAttribute.setHelpText("The user attribute to check against the resource attribute.");

        configProperties.add(departmentAttribute);

        return configProperties;
    }



    @Override
    public void close() {}
}

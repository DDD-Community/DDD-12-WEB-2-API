package com.moyorak.api.auth.domain;

import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserPrincipal implements OAuth2User, UserDetails {

    private Long id;

    private String email;

    private String name;

    private Map<String, Object> attributes;

    public static UserPrincipal newUserGenerate(
            final String email, final String name, final Map<String, Object> attributes) {
        UserPrincipal userPrincipal = new UserPrincipal();

        userPrincipal.email = email;
        userPrincipal.name = name;
        userPrincipal.attributes = attributes;

        return userPrincipal;
    }

    public static UserPrincipal generate(
            final Long id,
            final String email,
            final String name,
            final Map<String, Object> attributes) {
        UserPrincipal userPrincipal = new UserPrincipal();

        userPrincipal.id = id;
        userPrincipal.email = email;
        userPrincipal.name = name;
        userPrincipal.attributes = attributes;

        return userPrincipal;
    }

    public static UserPrincipal generate(final Long id, final String email, final String name) {
        return generate(id, email, name, Map.of());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

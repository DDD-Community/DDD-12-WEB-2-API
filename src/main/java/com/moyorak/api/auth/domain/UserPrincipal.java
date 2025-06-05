package com.moyorak.api.auth.domain;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

    private Long id;

    private String email;

    private String name;

    public static UserPrincipal generate(final Long id, final String email, final String name) {
        UserPrincipal userPrincipal = new UserPrincipal();

        userPrincipal.id = id;
        userPrincipal.email = email;
        userPrincipal.name = name;

        return userPrincipal;
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

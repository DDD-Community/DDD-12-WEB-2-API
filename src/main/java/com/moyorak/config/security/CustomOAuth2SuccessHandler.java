package com.moyorak.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyorak.api.auth.dto.SignInResponse;
import com.moyorak.api.auth.dto.SignUpResponse;
import com.moyorak.api.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        final OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        final String name = oAuth2User.getAttribute("name");
        final Long id = oAuth2User.getAttribute("id");

        final Boolean isNew = oAuth2User.getAttribute("isNew");

        if (isNew) {
            final SignUpResponse signUpResponse = new SignUpResponse(id, name);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(objectMapper.writeValueAsString(signUpResponse));

            return;
        }

        final SignInResponse signInResponse = authService.generate(id);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(signInResponse));
    }
}

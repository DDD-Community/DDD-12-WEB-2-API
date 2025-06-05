package com.moyorak.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.auth.dto.SignInResponse;
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

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        final OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        final String email = oAuth2User.getAttribute("email");
        final String name = oAuth2User.getAttribute("name");
        final Long id = oAuth2User.getAttribute("id");

        final String token =
                jwtTokenProvider.generateAccessToken(UserPrincipal.generate(id, email, name));

        final SignInResponse signInResponse = new SignInResponse(token);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(signInResponse));
    }
}

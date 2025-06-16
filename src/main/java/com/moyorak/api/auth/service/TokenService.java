package com.moyorak.api.auth.service;

import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.auth.domain.UserToken;
import com.moyorak.api.auth.dto.SignInResponse;
import com.moyorak.api.auth.repository.TokenRepository;
import com.moyorak.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignInResponse generate(final Long userId, final String email, final String name) {
        final UserPrincipal userPrincipal = UserPrincipal.generate(userId, email, name);

        final String token = jwtTokenProvider.generateAccessToken(userPrincipal);

        tokenRepository.save(UserToken.create(userId, token));

        return new SignInResponse(token);
    }
}

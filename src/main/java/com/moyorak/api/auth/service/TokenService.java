package com.moyorak.api.auth.service;

import com.moyorak.api.auth.domain.User;
import com.moyorak.api.auth.domain.UserNotFoundException;
import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.auth.domain.UserToken;
import com.moyorak.api.auth.dto.SignInResponse;
import com.moyorak.api.auth.repository.TokenRepository;
import com.moyorak.api.auth.repository.UserRepository;
import com.moyorak.config.exception.BusinessException;
import com.moyorak.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignInResponse generate(final Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        final UserPrincipal userPrincipal =
                UserPrincipal.generate(userId, user.getEmail(), user.getName());

        final String token = jwtTokenProvider.generateAccessToken(userPrincipal);

        tokenRepository.save(UserToken.create(userId, token));

        return new SignInResponse(token);
    }

    @Transactional
    public void signOut(final Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        UserToken userToken =
                tokenRepository
                        .findFirstByUserIdOrderByIdDesc(userId)
                        .orElseThrow(() -> new BusinessException("로그인 정보가 존재하지 않습니다."));

        if (userToken.isInValidToken()) {
            throw new BusinessException("로그인 정보가 존재하지 않습니다.");
        }

        userToken.clear();
    }
}

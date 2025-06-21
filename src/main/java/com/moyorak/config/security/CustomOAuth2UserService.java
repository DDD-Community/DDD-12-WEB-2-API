package com.moyorak.config.security;

import com.moyorak.api.auth.domain.User;
import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.auth.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        final String email = oauth2User.getAttribute("email");
        validEmail(email);

        final String name = oauth2User.getAttribute("name");
        final String provider = userRequest.getClientRegistration().getRegistrationId();
        validProvider(provider);

        final Optional<User> user = userRepository.findByEmail(email);

        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());

        if (user.isPresent()) {
            attributes.put("isNew", false);

            return UserPrincipal.generate(user.get().getId(), email, name, attributes);
        }

        attributes.put("isNew", true);

        return UserPrincipal.newUserGenerate(email, name, attributes);
    }

    private void validEmail(final String email) {
        if (!StringUtils.hasText(email)) {
            throw new OAuth2AuthenticationException("이메일 정보가 없습니다.");
        }
    }

    private void validProvider(final String provider) {
        if (!"google".equals(provider)) {
            throw new OAuth2AuthenticationException("지원하지 않는 플랫폼입니다.");
        }
    }
}

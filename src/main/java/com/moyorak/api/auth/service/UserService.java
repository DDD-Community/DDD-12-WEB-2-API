package com.moyorak.api.auth.service;

import com.moyorak.api.auth.domain.User;
import com.moyorak.api.auth.dto.SignUpRequest;
import com.moyorak.api.auth.repository.UserRepository;
import com.moyorak.config.exception.BusinessException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signUp(final SignUpRequest request) {
        // 등록된 이메일인지 확인
        final boolean isRegistered =
                userRepository.findByEmailAndUse(request.email(), true).isPresent();

        if (isRegistered) {
            throw new BusinessException("중복된 이메일입니다.");
        }

        final User user = request.toEntity();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Map<Long, User> getUsersAsMap(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return StreamSupport.stream(userRepository.findAllById(userIds).spliterator(), false)
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }
}

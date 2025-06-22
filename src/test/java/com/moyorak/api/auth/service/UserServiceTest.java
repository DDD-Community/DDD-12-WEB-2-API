package com.moyorak.api.auth.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.moyorak.api.auth.domain.User;
import com.moyorak.api.auth.domain.UserFixture;
import com.moyorak.api.auth.dto.SignUpRequest;
import com.moyorak.api.auth.dto.SignUpRequestFixture;
import com.moyorak.api.auth.repository.UserRepository;
import com.moyorak.config.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks private UserService userService;

    @Mock private UserRepository userRepository;

    @Nested
    @DisplayName("회원 가입을 할 때,")
    class signUp {

        @Test
        @DisplayName("이미 등록된 이메일이라면 오류가 발생합니다.")
        void registered() {
            // given
            final String email = "gildong@gmail.com";

            final SignUpRequest request = SignUpRequestFixture.fixture(email);
            final User user = UserFixture.fixture(1L, email, "", "");

            given(userRepository.findByEmailAndUse(email, true)).willReturn(Optional.of(user));

            // when & then
            assertThatThrownBy(() -> userService.signUp(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("중복된 이메일입니다.");
        }

        @Test
        @DisplayName("성공적으로 회원가입을 합니다.")
        void success() {
            // given
            final String email = "gildong@gmail.com";

            final SignUpRequest request = SignUpRequestFixture.fixture(email);
            final User user =
                    UserFixture.fixture(1L, email, request.name(), request.profileImage());

            given(userRepository.findByEmailAndUse(email, true)).willReturn(Optional.empty());
            given(userRepository.save(any(User.class))).willReturn(user);

            // when & then
            assertDoesNotThrow(() -> userService.signUp(request));
        }
    }
}

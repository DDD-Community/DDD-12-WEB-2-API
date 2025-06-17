package com.moyorak.api.auth.repository;

import com.moyorak.api.auth.domain.UserToken;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<UserToken, Long> {

    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value =
                            "TokenRepository.findFirstByUserIdOrderByIdDesc : 회원 ID를 조건으로 가장 최근의 로그인 정보를 조회합니다."))
    Optional<UserToken> findFirstByUserIdOrderByIdDesc(Long userId);
}

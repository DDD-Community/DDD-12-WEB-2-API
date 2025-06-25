package com.moyorak.api.auth.repository;

import com.moyorak.api.auth.domain.UserDailyState;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserDailyStateRepository extends CrudRepository<UserDailyState, Long> {
    Optional<UserDailyState> findByUserIdAndRecordDate(Long userId, LocalDate recordDate);
}

package com.moyorak.api.auth.repository;

import com.moyorak.api.auth.domain.UserToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<UserToken, Long> {}

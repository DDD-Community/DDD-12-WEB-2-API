package com.moyorak.api.auth.repository;

import com.moyorak.api.auth.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {}

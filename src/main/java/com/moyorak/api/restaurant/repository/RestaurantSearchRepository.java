package com.moyorak.api.restaurant.repository;

import com.moyorak.api.restaurant.domain.RestaurantSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantSearchRepository extends JpaRepository<RestaurantSearch, Long> {}

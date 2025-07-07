package com.moyorak.api.restaurant.repository;

import com.moyorak.api.restaurant.domain.Restaurant;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByNameAndRoundedLongitudeAndRoundedLatitudeAndUseTrue(
            String name, BigDecimal roundedLongitude, BigDecimal roundedLatitude);

    Optional<Restaurant> findByIdAndUse(Long id, boolean use);
}

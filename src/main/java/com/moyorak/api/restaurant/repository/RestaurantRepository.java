package com.moyorak.api.restaurant.repository;

import com.moyorak.api.restaurant.domain.Restaurant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByKakaoPlaceIdAndUseTrue(String kakaoPlaceId);
}

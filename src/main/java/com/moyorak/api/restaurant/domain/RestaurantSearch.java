package com.moyorak.api.restaurant.domain;

import com.moyorak.infra.orm.AuditInformation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "restaurant_search")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantSearch extends AuditInformation {

    @Id
    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(nullable = false, name = "name")
    private String name;

    @Builder(access = AccessLevel.PRIVATE)
    private RestaurantSearch(Long restaurantId, String name) {
        this.restaurantId = restaurantId;
        this.name = name;
    }

    public static RestaurantSearch create(final Long restaurantId, final String name) {
        return RestaurantSearch.builder().restaurantId(restaurantId).name(name).build();
    }
}

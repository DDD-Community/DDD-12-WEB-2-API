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
import org.hibernate.annotations.Comment;

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

    @Comment("식당 주소")
    @Column(name = "road_address", nullable = false)
    private String roadAddress;

    @Builder(access = AccessLevel.PRIVATE)
    private RestaurantSearch(Long restaurantId, String name, String roadAddress) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.roadAddress = roadAddress;
    }

    public static RestaurantSearch create(
            final Long restaurantId, final String name, final String roadAddress) {
        return RestaurantSearch.builder()
                .restaurantId(restaurantId)
                .name(name)
                .roadAddress(roadAddress)
                .build();
    }
}

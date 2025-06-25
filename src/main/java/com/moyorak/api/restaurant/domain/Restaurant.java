package com.moyorak.api.restaurant.domain;

import com.moyorak.infra.orm.AuditInformation;
import com.moyorak.infra.orm.BooleanYnConverter;
import com.moyorak.infra.orm.RestaurantCategoryConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(
        name = "restaurant",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_restaurant_name_location",
                    columnNames = {"name", "rounded_longitude", "rounded_latitude"})
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("장소 링크 url")
    @Column(name = "place_url", length = 512)
    private String placeUrl;

    @Comment("식당 이름")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("식당 주소")
    @Column(name = "address", nullable = false)
    private String address;

    @Comment("식당 주소")
    @Column(name = "road_address", nullable = false)
    private String roadAddress;

    @Comment("식당 카테고리")
    @Convert(converter = RestaurantCategoryConverter.class)
    @Column(name = "category", nullable = false, length = 32)
    private RestaurantCategory category;

    @Comment("경도")
    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Comment("위도")
    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "rounded_longitude", precision = 8, scale = 5, nullable = false)
    private BigDecimal roundedLongitude;

    @Column(name = "rounded_latitude", precision = 8, scale = 5, nullable = false)
    private BigDecimal roundedLatitude;

    @Comment("사용 여부")
    @Convert(converter = BooleanYnConverter.class)
    @Column(name = "use_yn", nullable = false, columnDefinition = "char(1)")
    private boolean use = true;

    private static final int SCALE = 5;

    @Builder(access = AccessLevel.PRIVATE)
    private Restaurant(
            String placeUrl,
            String name,
            String address,
            String roadAddress,
            RestaurantCategory category,
            double longitude,
            double latitude) {
        this.placeUrl = placeUrl;
        this.name = name;
        this.address = address;
        this.category = category;
        this.longitude = longitude;
        this.latitude = latitude;
        this.roadAddress = roadAddress;
        roundedLongitude = roundToScale(longitude);
        roundedLatitude = roundToScale(latitude);
    }

    public static Restaurant create(
            String placeUrl,
            String name,
            String address,
            String roadAddress,
            RestaurantCategory category,
            double longitude,
            double latitude) {
        return Restaurant.builder()
                .placeUrl(placeUrl)
                .name(name)
                .address(address)
                .roadAddress(roadAddress)
                .category(category)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

    private BigDecimal roundToScale(final double value) {
        return BigDecimal.valueOf(value).setScale(SCALE, RoundingMode.HALF_UP);
    }
}

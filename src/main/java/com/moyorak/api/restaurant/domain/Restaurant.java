package com.moyorak.api.restaurant.domain;

import com.moyorak.infra.orm.AuditInformation;
import com.moyorak.infra.orm.BooleanYnConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("카카오 장소 고유 ID")
    @Column(name = "kakao_place_id", nullable = false, unique = true, length = 30)
    private String kakaoPlaceId;

    @Comment("카카오 장소 링크 url")
    @Column(name = "kakao_place_url", length = 512)
    private String kakaoPlaceUrl;

    @Comment("식당 이름")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("식당 주소")
    @Column(name = "address", nullable = false)
    private String address;

    @Comment("식당 카테고리")
    @Column(name = "category", nullable = false, length = 32)
    private RestaurantCategory category;

    @Comment("경도")
    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Comment("위도")
    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Comment("사용 여부")
    @Convert(converter = BooleanYnConverter.class)
    @Column(name = "use_yn", nullable = false, columnDefinition = "char(1)")
    private boolean use = true;

    @Builder(access = AccessLevel.PRIVATE)
    private Restaurant(
            String kakaoPlaceId,
            String kakaoPlaceUrl,
            String name,
            String address,
            RestaurantCategory category,
            double longitude,
            double latitude) {
        this.kakaoPlaceId = kakaoPlaceId;
        this.kakaoPlaceUrl = kakaoPlaceUrl;
        this.name = name;
        this.address = address;
        this.category = category;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static Restaurant create(
            String kakaoPlaceId,
            String kakaoPlaceUrl,
            String name,
            String address,
            RestaurantCategory category,
            double longitude,
            double latitude) {
        return Restaurant.builder()
                .kakaoPlaceId(kakaoPlaceId)
                .kakaoPlaceUrl(kakaoPlaceUrl)
                .name(name)
                .address(address)
                .category(category)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}

package com.moyorak.api.sample.domain;

import com.moyorak.api.infra.orm.AuditInformation;
import com.moyorak.api.infra.orm.BooleanYnConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.ObjectUtils;

@Getter
@Entity
@Table(name = "sample")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sample extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Comment("제목")
    @Column(name = "title", nullable = false, length = 64)
    private String title;

    @NotNull
    @Comment("내용")
    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @NotNull
    @Comment("사용 여부")
    @Convert(converter = BooleanYnConverter.class)
    @Column(name = "use_yn", nullable = false, columnDefinition = "char(1)")
    private boolean isUse = true;

    public static Sample create(final String title, final String content) {
        Sample sample = new Sample();

        sample.title = title;
        sample.content = content;

        return sample;
    }

    public void modify(final String title, final String content) {
        if (!ObjectUtils.isEmpty(title)) {
            this.title = title;
        }

        if (!ObjectUtils.isEmpty(content)) {
            this.content = content;
        }
    }
}

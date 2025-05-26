package com.moyorak.api.infra.orm;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditInformation {

    @CreatedDate
    @Comment("생성 일자")
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    //    @CreatedBy
    //    @Comment("생성자")
    //    @Column(name = "CREATED_ID", nullable = false, updatable = false)
    //    private Long createdId;

    @LastModifiedDate
    @Comment("수정 일자")
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updatedDate;

    //    @LastModifiedBy
    //    @Comment("수정자")
    //    @Column(name = "UPDATE_ID")
    //    private Long updatedId;
}

package com.moyorak.api.auth.domain;

import com.moyorak.infra.orm.AuditInformation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "user_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Comment("이메일")
    @Column(name = "email", nullable = false, unique = true, columnDefinition = "varchar(64)")
    private String email;

    @Comment("회원 이름")
    @Column(name = "name", nullable = false, columnDefinition = "varchar(32)")
    private String name;

    @Builder(access = AccessLevel.PRIVATE)
    protected User(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static User registeredUser(final String email, final String name) {
        return User.builder().email(email).name(name).build();
    }
}

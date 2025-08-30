package dmu.dasom.api.domain.member.entity;

import dmu.dasom.api.domain.common.BaseEntity;
import dmu.dasom.api.domain.member.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "email", unique = true, length = 64, nullable = false)
    @Email
    private String email;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Builder.Default
    @Column(name = "role", length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_MEMBER;

    // 기수 정보를 저장할 필드 추가
    @Column(name = "generation", length = 4, nullable = false)
    private String generation;
}
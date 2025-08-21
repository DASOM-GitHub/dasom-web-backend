package dmu.dasom.api.domain.executive.entity;

import dmu.dasom.api.domain.common.BaseEntity; // BaseEntity 상속 받음
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*; // JPA 어노테이션 패키지 ( DB 매핑 관련 )
import lombok.*; // 보일러플레이트 코드 자동 생성 라이브러리

@Getter
@Entity
@Table(name = "executive")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "조직도 엔티티")
public class ExecutiveEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름
    @Column(nullable = false, length = 50)
    private String name;

    // 직책
    @Column(nullable=false, length = 50)
    private String position;

    // 깃허브 주소
    @Column(nullable=false, length = 255)
    private String githubUrl;
}

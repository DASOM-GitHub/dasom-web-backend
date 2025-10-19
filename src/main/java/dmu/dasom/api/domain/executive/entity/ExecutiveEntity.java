package dmu.dasom.api.domain.executive.entity;

import dmu.dasom.api.domain.common.BaseEntity; // BaseEntity 상속 받음
import dmu.dasom.api.domain.executive.dto.ExecutiveListResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveUpdateRequestDto;
import dmu.dasom.api.domain.executive.enums.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*; // JPA 어노테이션 패키지 ( DB 매핑 관련 )
import lombok.*; // 보일러플레이트 코드 자동 생성 라이브러리
import org.checkerframework.checker.units.qual.C;

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

    // 역할
    @Column(nullable = false, length = 50)
    private String role;

    // 깃허브 이름
    @Column(name = "github_username")
    private String githubUsername;

    // 소속팀 (president/tech/academic/pr/management)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Team team;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 9999;

    // 엔티티 업데이트 메소드
    public void update(ExecutiveUpdateRequestDto dto) {
        if (dto.getName() != null) this.name = dto.getName();
        if (dto.getPosition() != null) this.position = dto.getPosition();
        if (dto.getRole() != null) this.role = dto.getRole();
        if (dto.getGithub_username() != null) this.githubUsername = dto.getGithub_username();
        if (dto.getTeam() != null) this.team = dto.getTeam();
        if (dto.getSortOrder() != null) this.sortOrder = dto.getSortOrder();
    }

    @PrePersist
    public void prePersist() {
        if (sortOrder == null) sortOrder = 9999;
    }
    @PreUpdate
    public void preUpdate() {
        if (sortOrder == null) sortOrder = 9999;
    }

    // 엔티티 -> DTO 변환 책임
    public ExecutiveResponseDto toResponseDto() {
        return ExecutiveResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .position(this.position)
                .role(this.role)
                .github_username(this.githubUsername)
                .team(this.team)
                .sortOrder(this.sortOrder)
                .build();
    }

    // 임원진 전체 목록 조회
    public ExecutiveListResponseDto toListResponseDto() {
        return ExecutiveListResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .position(this.position)
                .role(this.role)
                .github_username(this.githubUsername)
                .team(this.team)
                .sortOrder(this.sortOrder)
                .build();
    }
}

package dmu.dasom.api.domain.activity.dto;

import dmu.dasom.api.domain.activity.entity.Section;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(name = "SectionItemDto", description = "섹션별 활동 목록")
public class SectionItemDto {

    @Schema(description = "활동 섹션 고유 ID", example = "1")
    private final Long id;

    @Schema(description = "활동 섹션", example = "교내 경진대회")
    private final String section;

    @Schema(description = "활동 목록")
    private final List<ActivityItemDto> activities;

    public static SectionItemDto of(Section section, List<ActivityItemDto> activities) {
        return SectionItemDto.builder()
                .id(section.getId())
                .section(section.getName())
                .activities(activities)
                .build();
    }
}

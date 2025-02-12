package dmu.dasom.api.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@Schema(name = "PageResponse", description = "페이징 응답 DTO")
public class PageResponse<T> {

    @Schema(description = "데이터")
    @NotEmpty
    private List<T> content;

    @Schema(description = "현재 페이지 번호")
    private int number;

    @Schema(description = "페이지 크기")
    private int size;

    @Schema(description = "전체 데이터 개수")
    private long totalElements;

    @Schema(description = "전체 페이지 개수")
    private int totalPages;

    @Schema(description = "첫 페이지 여부")
    private boolean first;

    @Schema(description = "마지막 페이지 여부")
    private boolean last;

    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .number(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

}

package dmu.dasom.api.domain.news;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.news.dto.NewsRequestDto;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import dmu.dasom.api.domain.news.entity.NewsEntity;
import dmu.dasom.api.domain.news.repository.NewsRepository;
import dmu.dasom.api.domain.news.service.NewsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsService newsService;

    @Test
    @DisplayName("뉴스 전체 조회 테스트")
    void getAllNews() {
        // Given
        NewsEntity news1 = new NewsEntity(1L, "뉴스1", "내용1", "url1");
        NewsEntity news2 = new NewsEntity(2L, "뉴스2", "내용2", "url2");

        when(newsRepository.findAll()).thenReturn(List.of(news1, news2));

        // When
        List<NewsResponseDto> newsList = newsService.getAllNews();

        // Then
        assertThat(newsList).hasSize(2);
        assertThat(newsList.get(0).getTitle()).isEqualTo("뉴스1");
        verify(newsRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("뉴스 개별 조회 - 성공")
    void getNewsById_Success() {
        // Given
        Long id = 1L;
        NewsEntity news = new NewsEntity(id, "뉴스1", "내용1", "url1");

        when(newsRepository.findById(id)).thenReturn(Optional.of(news));

        // When
        NewsResponseDto responseDto = newsService.getNewsById(id);

        // Then
        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getTitle()).isEqualTo("뉴스1");
        verify(newsRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("뉴스 개별 조회 - 실패 (존재하지 않는 ID)")
    void getNewsById_NotFound() {
        // Given
        Long id = 999L;

        when(newsRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> newsService.getNewsById(id));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        verify(newsRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("뉴스 생성 테스트")
    void createNews() {
        // Given
        NewsRequestDto requestDto = new NewsRequestDto("새 뉴스", "새 내용", "새 이미지");

        NewsEntity news = NewsEntity.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imageUrl(requestDto.getImageUrl())
                .build();

        NewsEntity savedNews = new NewsEntity(1L, news.getTitle(), news.getContent(), news.getImageUrl());

        when(newsRepository.save(any(NewsEntity.class))).thenReturn(savedNews);

        // When
        NewsResponseDto responseDto = newsService.createNews(requestDto);

        // Then
        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getTitle()).isEqualTo("새 뉴스");
        assertThat(responseDto.getContent()).isEqualTo("새 내용");
        assertThat(responseDto.getImageUrl()).isEqualTo("새 이미지");

        verify(newsRepository, times(1)).save(any(NewsEntity.class));
    }

    @Test
    @DisplayName("뉴스 수정 테스트")
    void updateNews() {
        // Given
        Long id = 1L;
        NewsEntity existingNews = new NewsEntity(id, "기존 뉴스", "기존 내용", "기존 이미지");

        NewsRequestDto updateRequest = new NewsRequestDto("수정된 뉴스", "수정된 내용", "수정된 이미지");

        when(newsRepository.findById(id)).thenReturn(Optional.of(existingNews));

        // When
        NewsResponseDto updatedNews = newsService.updateNews(id, updateRequest);

        // Then
        assertThat(updatedNews.getTitle()).isEqualTo("수정된 뉴스");
        assertThat(updatedNews.getContent()).isEqualTo("수정된 내용");
        assertThat(updatedNews.getImageUrl()).isEqualTo("수정된 이미지");

        verify(newsRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("뉴스 삭제 테스트")
    void deleteNews() {
        // Given
        Long id = 1L;
        NewsEntity existingNews = new NewsEntity(id, "삭제할 뉴스", "삭제할 내용", "삭제할 이미지");

        when(newsRepository.findById(id)).thenReturn(Optional.of(existingNews));
        doNothing().when(newsRepository).delete(existingNews);

        // When
        newsService.deleteNews(id);

        // Then
        verify(newsRepository, times(1)).findById(id);
        verify(newsRepository, times(1)).delete(existingNews);
    }

}
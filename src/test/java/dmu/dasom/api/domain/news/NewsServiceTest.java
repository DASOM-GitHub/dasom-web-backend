package dmu.dasom.api.domain.news;

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
        NewsEntity news1 = NewsEntity.builder().id(1L).title("뉴스1").content("내용1").imageUrl("url1").build();
        NewsEntity news2 = NewsEntity.builder().id(2L).title("뉴스2").content("내용2").imageUrl("url2").build();

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
        NewsEntity news = NewsEntity.builder().id(id).title("뉴스1").content("내용1").imageUrl("url1").build();

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
        Exception exception = assertThrows(IllegalArgumentException.class, () -> newsService.getNewsById(id));
        assertThat(exception.getMessage()).isEqualTo("해당 뉴스가 존재하지 않습니다. ID: " + id);
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

        NewsEntity savedNews = NewsEntity.builder()
                .id(1L)
                .title(news.getTitle())
                .content(news.getContent())
                .imageUrl(news.getImageUrl())
                .build();

        when(newsRepository.save(any(NewsEntity.class))).thenReturn(savedNews);

        // When
        NewsResponseDto responseDto = newsService.createNews(requestDto);

        // Then
        assertThat(responseDto.getId()).isEqualTo(1L); // 저장된 뉴스의 ID 할당확인
        assertThat(responseDto.getTitle()).isEqualTo("새 뉴스");
        assertThat(responseDto.getContent()).isEqualTo("새 내용");
        assertThat(responseDto.getImageUrl()).isEqualTo("새 이미지");

        verify(newsRepository, times(1)).save(any(NewsEntity.class)); // save() 호출 검증
    }

}

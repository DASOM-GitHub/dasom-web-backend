package dmu.dasom.api.domain.news;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.news.dto.NewsRequestDto;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import dmu.dasom.api.domain.news.entity.NewsEntity;
import dmu.dasom.api.domain.news.repository.NewsRepository;
import dmu.dasom.api.domain.news.service.NewsImageService;
import dmu.dasom.api.domain.news.service.NewsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsImageService newsImageService;

    @InjectMocks
    private NewsService newsService;

    @Test
    @DisplayName("뉴스 전체 조회 - 성공")
    void getAllNews_success() {
        // Given
        NewsEntity news1 = new NewsEntity(1L, "뉴스1", "내용1", List.of("url1"));
        NewsEntity news2 = new NewsEntity(2L, "뉴스2", "내용2", List.of("url2"));

        when(newsRepository.findAll()).thenReturn(List.of(news1, news2));

        // When
        List<NewsResponseDto> newsList = newsService.getAllNews();

        // Then
        assertThat(newsList).hasSize(2);
        assertThat(newsList.get(0).getTitle()).isEqualTo("뉴스1");
        assertThat(newsList.get(1).getTitle()).isEqualTo("뉴스2");

        verify(newsRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("뉴스 개별 조회 - 성공")
    void getNewsById_success() {
        // Given
        Long id = 1L;
        NewsEntity news = new NewsEntity(id, "뉴스1", "내용1", List.of("url1"));

        when(newsRepository.findById(id)).thenReturn(Optional.of(news));

        // When
        NewsResponseDto responseDto = newsService.getNewsById(id);

        // Then
        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getTitle()).isEqualTo("뉴스1");

        verify(newsRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("뉴스 개별 조회 - 실패 (존재하지 않는 뉴스)")
    void getNewsById_fail() {
        // Given
        Long id = 999L;
        when(newsRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            newsService.getNewsById(id);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        verify(newsRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("뉴스 생성 - 성공")
    void createNews_success() {
        // Given
        MultipartFile file1 = new MockMultipartFile("file", "image1.jpg", "image/jpeg", "새 이미지1".getBytes());
        MultipartFile file2 = new MockMultipartFile("file", "image2.jpg", "image/jpeg", "새 이미지2".getBytes());

        List<MultipartFile> imageFiles = List.of(file1, file2);
        List<String> uploadedImageUrls = List.of("url1", "url2");

        NewsRequestDto requestDto = new NewsRequestDto("새 뉴스", "새 내용", imageFiles);

        when(newsImageService.uploadImages(imageFiles)).thenReturn(uploadedImageUrls);
        when(newsRepository.save(any(NewsEntity.class)))
                .thenAnswer(invocation -> {
                    NewsEntity news = invocation.getArgument(0);
                    return new NewsEntity(1L, news.getTitle(), news.getContent(), news.getImageUrls());
                });

        // When
        NewsResponseDto responseDto = newsService.createNews(requestDto, imageFiles);

        // Then
        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getTitle()).isEqualTo("새 뉴스");
        assertThat(responseDto.getContent()).isEqualTo("새 내용");
        assertThat(responseDto.getImageUrls()).containsExactlyElementsOf(uploadedImageUrls);

        verify(newsRepository, times(1)).save(any(NewsEntity.class));
    }

    @Test
    @DisplayName("뉴스 수정 - 성공")
    void updateNews_success() {
        // Given
        Long id = 1L;
        NewsEntity existingNews = new NewsEntity(id, "기존 뉴스", "기존 내용", List.of("기존 이미지"));

        MultipartFile file1 = new MockMultipartFile("file", "updated_image1.jpg", "image/jpeg", "수정된 이미지1".getBytes());
        MultipartFile file2 = new MockMultipartFile("file", "updated_image2.jpg", "image/jpeg", "수정된 이미지2".getBytes());

        List<MultipartFile> updatedImageFiles = List.of(file1, file2);
        List<String> updatedImageUrls = List.of("updated_url1", "updated_url2");

        NewsRequestDto updateRequest = new NewsRequestDto("수정된 뉴스", "수정된 내용", updatedImageFiles);

        when(newsRepository.findById(id)).thenReturn(Optional.of(existingNews));
        when(newsImageService.uploadImages(updatedImageFiles)).thenReturn(updatedImageUrls);

        // When
        NewsResponseDto updatedNews = newsService.updateNews(id, updateRequest, updatedImageFiles);

        // Then
        assertThat(updatedNews.getTitle()).isEqualTo("수정된 뉴스");
        assertThat(updatedNews.getContent()).isEqualTo("수정된 내용");
        assertThat(updatedNews.getImageUrls()).containsExactlyElementsOf(updatedImageUrls);

        verify(newsRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("뉴스 삭제 - 성공")
    void deleteNews_success() {
        // Given
        Long id = 1L;
        NewsEntity existingNews = new NewsEntity(id, "삭제할 뉴스", "삭제할 내용", List.of("삭제할 이미지"));

        when(newsRepository.findById(id)).thenReturn(Optional.of(existingNews));
        doNothing().when(newsRepository).delete(existingNews);

        // When
        newsService.deleteNews(id);

        // Then
        verify(newsRepository, times(1)).findById(id);
        verify(newsRepository, times(1)).delete(existingNews);
    }

    @Test
    @DisplayName("뉴스 삭제 - 실패 (존재하지 않는 뉴스)")
    void deleteNews_fail() {
        // Given
        Long id = 999L;
        when(newsRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            newsService.deleteNews(id);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        verify(newsRepository, times(1)).findById(id);
        verify(newsRepository, never()).delete(any());
    }

}
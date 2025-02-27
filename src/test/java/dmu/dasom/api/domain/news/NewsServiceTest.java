package dmu.dasom.api.domain.news;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.global.file.entity.FileEntity;
import dmu.dasom.api.global.file.repository.FileRepository;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private NewsService newsService;

    @Test
    @DisplayName("뉴스 전체 조회 - 성공")
    void getAllNews_success() {
        // Given
        List<String> imageUrls1 = List.of("/path/image1.jpg");
        List<String> imageUrls2 = List.of("/path/image2.jpg");

        NewsEntity news1 = new NewsEntity(1L, "뉴스1", "내용1", imageUrls1);
        NewsEntity news2 = new NewsEntity(2L, "뉴스2", "내용2", imageUrls2);

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
        List<String> imageUrls = List.of("/path/image1.jpg");
        NewsEntity news = new NewsEntity(id, "뉴스1", "내용1", imageUrls);

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
        CustomException exception = assertThrows(CustomException.class, () -> newsService.getNewsById(id));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        verify(newsRepository, times(1)).findById(id);
    }
//
//    @Test
//    @DisplayName("뉴스 생성 - 성공")
//    void createNews_success() {
//        // Given
//        List<Long> fileIds = List.of(1L, 2L);
//        List<FileEntity> fileEntities = List.of(
//                FileEntity file1 = new FileEntity(1L, "image1.jpg", "stored_image1.jpg", "/path/image1.jpg", "image/jpeg", 1024L);
//                FileEntity file2 = new FileEntity(2L, "image2.jpg", "stored_image2.jpg", "/path/image2.jpg", "image/png", 2048L);
//
//        );
//
//        List<String> imageUrls = fileEntities.stream()
//                .map(FileEntity::getFilePath)
//                .collect(Collectors.toList());
//
//        NewsRequestDto requestDto = new NewsRequestDto("새 뉴스", "새 내용", fileIds);
//
//        when(fileRepository.findAllById(anyIterable())).thenReturn(fileEntities);
//        when(newsRepository.save(any(NewsEntity.class)))
//                .thenAnswer(invocation -> {
//                    NewsEntity news = invocation.getArgument(0);
//                    return new NewsEntity(1L, news.getTitle(), news.getContent(), imageUrls);
//                });
//
//        // When
//        NewsResponseDto responseDto = newsService.createNews(requestDto);
//
//        // Then
//        assertThat(responseDto.getId()).isEqualTo(1L);
//        assertThat(responseDto.getTitle()).isEqualTo("새 뉴스");
//        assertThat(responseDto.getContent()).isEqualTo("새 내용");
//        assertThat(responseDto.getImageUrls()).containsExactly("/path/image1.jpg", "/path/image2.jpg");
//
//        verify(newsRepository, times(1)).save(any(NewsEntity.class));
//    }

//    @Test
//    @DisplayName("뉴스 수정 - 성공")
//    void updateNews_success() {
//        // Given
//        Long id = 1L;
//        List<String> oldImageUrls = List.of("/path/old_image.jpg");
//        NewsEntity existingNews = new NewsEntity(id, "기존 뉴스", "기존 내용", oldImageUrls);
//
//        List<Long> updatedFileIds = List.of(3L, 4L);
//        List<FileEntity> updatedFiles = List.of(
//                new FileEntity(3L, "updated_image1.jpg", "stored_updated_image1.jpg", "/path/updated_image1.jpg", "image/jpeg", 1024L),
//                new FileEntity(4L, "updated_image2.jpg", "stored_updated_image2.jpg", "/path/updated_image2.jpg", "image/jpeg", 2048L)
//        );
//
//        List<String> updatedImageUrls = updatedFiles.stream()
//                .map(FileEntity::getFilePath)
//                .collect(Collectors.toList());
//
//        NewsRequestDto updateRequest = new NewsRequestDto("수정된 뉴스", "수정된 내용", updatedFileIds);
//
//        when(newsRepository.findById(id)).thenReturn(Optional.of(existingNews));
//        when(fileRepository.findAllById(updatedFileIds)).thenReturn(updatedFiles);
//
//        // When
//        NewsResponseDto updatedNews = newsService.updateNews(id, updateRequest);
//
//        // Then
//        assertThat(updatedNews.getTitle()).isEqualTo("수정된 뉴스");
//        assertThat(updatedNews.getContent()).isEqualTo("수정된 내용");
//        assertThat(updatedNews.getImageUrls()).containsExactlyElementsOf(updatedImageUrls);
//
//        verify(newsRepository, times(1)).findById(id);
//    }

    @Test
    @DisplayName("뉴스 삭제 - 성공")
    void deleteNews_success() {
        // Given
        Long id = 1L;
        NewsEntity existingNews = new NewsEntity(id, "삭제할 뉴스", "삭제할 내용", List.of());

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
        CustomException exception = assertThrows(CustomException.class, () -> newsService.deleteNews(id));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        verify(newsRepository, times(1)).findById(id);
        verify(newsRepository, never()).delete(any());
    }

}
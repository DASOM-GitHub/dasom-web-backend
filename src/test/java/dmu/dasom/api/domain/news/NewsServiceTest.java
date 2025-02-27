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
    @DisplayName("뉴스 개별 조회 - 성공")
    void getNewsById_success() {
        // Given
        Long id = 1L;
        List<FileEntity> images = List.of(
                FileEntity.builder()
                        .id(1L)
                        .originalName("image1.jpg")
                        .base64Data("base64_encoded_data")
                        .fileType("image/jpeg")
                        .fileSize(1024L)
                        .build()
        );

        NewsEntity news = NewsEntity.builder()
                .id(id)
                .title("뉴스1")
                .content("내용1")
                .images(images)
                .build();

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

    @Test
    @DisplayName("뉴스 생성 - 성공")
    void createNews_success() {
        // Given
        List<Long> fileIds = List.of(1L, 2L);
        List<FileEntity> fileEntities = List.of(
                FileEntity.builder()
                        .id(1L)
                        .originalName("image1.jpg")
                        .base64Data("base64_encoded_data1")
                        .fileType("image/jpeg")
                        .fileSize(1024L)
                        .build(),

                FileEntity.builder()
                        .id(2L)
                        .originalName("image2.jpg")
                        .base64Data("base64_encoded_data2")
                        .fileType("image/png")
                        .fileSize(2048L)
                        .build()
        );

        NewsRequestDto requestDto = new NewsRequestDto("새 뉴스", "새 내용", fileIds);

        when(fileRepository.findAllById(anyIterable())).thenReturn(fileEntities);
        when(newsRepository.save(any(NewsEntity.class)))
                .thenAnswer(invocation -> {
                    NewsEntity news = invocation.getArgument(0);
                    return NewsEntity.builder()
                            .id(1L)
                            .title(news.getTitle())
                            .content(news.getContent())
                            .images(news.getImages())
                            .build();
                });

        // When
        NewsResponseDto responseDto = newsService.createNews(requestDto);

        // Then
        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getTitle()).isEqualTo("새 뉴스");
        assertThat(responseDto.getContent()).isEqualTo("새 내용");

        verify(newsRepository, times(1)).save(any(NewsEntity.class));
    }

    @Test
    @DisplayName("뉴스 수정 - 성공")
    void updateNews_success() {
        // Given
        Long id = 1L;
        List<FileEntity> oldImages = List.of(
                FileEntity.builder()
                        .id(1L)
                        .originalName("old_image.jpg")
                        .base64Data("old_base64_data")
                        .fileType("image/jpeg")
                        .fileSize(1024L)
                        .build()
        );

        NewsEntity existingNews = NewsEntity.builder()
                .id(id)
                .title("기존 뉴스")
                .content("기존 내용")
                .images(oldImages)
                .build();

        List<Long> updatedFileIds = List.of(3L, 4L);
        List<FileEntity> updatedFiles = List.of(
                FileEntity.builder()
                        .id(3L)
                        .originalName("updated_image1.jpg")
                        .base64Data("updated_base64_data1")
                        .fileType("image/jpeg")
                        .fileSize(1024L)
                        .build(),

                FileEntity.builder()
                        .id(4L)
                        .originalName("updated_image2.jpg")
                        .base64Data("updated_base64_data2")
                        .fileType("image/jpeg")
                        .fileSize(2048L)
                        .build()
        );

        NewsRequestDto updateRequest = new NewsRequestDto("수정된 뉴스", "수정된 내용", updatedFileIds);

        when(newsRepository.findById(id)).thenReturn(Optional.of(existingNews));
        when(fileRepository.findAllById(updatedFileIds)).thenReturn(updatedFiles);

        // When
        NewsResponseDto updatedNews = newsService.updateNews(id, updateRequest);

        // Then
        assertThat(updatedNews.getTitle()).isEqualTo("수정된 뉴스");
        assertThat(updatedNews.getContent()).isEqualTo("수정된 내용");

        verify(newsRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("뉴스 삭제 - 성공")
    void deleteNews_success() {
        // Given
        Long id = 1L;
        NewsEntity existingNews = NewsEntity.builder()
                .id(id)
                .title("삭제할 뉴스")
                .content("삭제할 내용")
                .build();

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
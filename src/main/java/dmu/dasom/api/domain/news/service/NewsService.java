package dmu.dasom.api.domain.news.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.news.dto.NewsRequestDto;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import dmu.dasom.api.domain.news.entity.NewsEntity;
import dmu.dasom.api.domain.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsImageService newsImageService;

    // 전체 조회
    public List<NewsResponseDto> getAllNews() {
        return newsRepository.findAll().stream()
                .map(NewsEntity::toResponseDto)
                .collect(Collectors.toList());
    }

    // 개별 조회
    public NewsResponseDto getNewsById(Long id) {
        return newsRepository.findById(id)
                .map(NewsEntity::toResponseDto)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    // 생성
    @Transactional
    public NewsResponseDto createNews(NewsRequestDto requestDto, List<MultipartFile> imageFiles) {
        // 이미지 업로드 후 URL 리스트 받아오기
        List<String> uploadedImageUrls = newsImageService.uploadImages(imageFiles);  // URL 리스트 생성

        NewsEntity news = NewsEntity.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imageUrls(uploadedImageUrls)  // 이미지 URL 리스트 저장
                .build();

        NewsEntity savedNews = newsRepository.save(news);
        return savedNews.toResponseDto();
    }

    // 수정
    @Transactional
    public NewsResponseDto updateNews(Long id, NewsRequestDto requestDto, List<MultipartFile> imageFiles) {
        // 뉴스 엔티티 찾기
        NewsEntity news = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 이미지 업로드 후 URL 리스트 받아오기
        List<String> uploadedImageUrls = newsImageService.uploadImages(imageFiles);

        news.update(requestDto.getTitle(), requestDto.getContent(), uploadedImageUrls);

        return news.toResponseDto();
    }

    // 삭제
    @Transactional
    public void deleteNews(Long id) {
        NewsEntity news = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        newsRepository.delete(news);
    }

}
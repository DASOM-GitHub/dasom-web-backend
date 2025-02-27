package dmu.dasom.api.domain.news.service;

import dmu.dasom.api.global.file.dto.FileResponseDto;
import dmu.dasom.api.global.file.entity.FileEntity;
import dmu.dasom.api.global.file.service.FileService;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.news.dto.NewsRequestDto;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import dmu.dasom.api.domain.news.entity.NewsEntity;
import dmu.dasom.api.domain.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final FileService fileService;

    // 전체 뉴스 조회
    public List<NewsResponseDto> getAllNews() {
        return newsRepository.findAll().stream()
                .map(news -> NewsResponseDto.builder()
                        .id(news.getId())
                        .title(news.getTitle())
                        .content(news.getContent())
                        .createdAt(news.getCreatedAt())
                        .imageUrls(news.getImageUrls())
                        .build())
                .collect(Collectors.toList());
    }

    // 개별 뉴스 조회
    public NewsResponseDto getNewsById(Long id) {
        NewsEntity news = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        return NewsResponseDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .createdAt(news.getCreatedAt())
                .imageUrls(news.getImageUrls())
                .build();
    }

    // 뉴스 생성
    @Transactional
    public NewsResponseDto createNews(NewsRequestDto requestDto) {
        List<FileResponseDto> uploadedFiles = fileService.getFilesByIds(requestDto.getFileIds());

        List<String> base64Images = uploadedFiles.stream()
                .map(file -> "data:" + file.getFileType() + ";base64," + file.getBase64Data())
                .collect(Collectors.toList());

        NewsEntity news = NewsEntity.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imageUrls(base64Images)
                .build();

        return newsRepository.save(news).toResponseDto();
    }

    // 뉴스 수정
    @Transactional
    public NewsResponseDto updateNews(Long id, NewsRequestDto requestDto) {
        NewsEntity news = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // fileEntity -> response로 수정
        List<FileResponseDto> uploadedFiles = fileService.getFilesByIds(requestDto.getFileIds());

        List<String> base64Images = uploadedFiles.stream()
                .map(file -> "data:" + file.getFileType() + ";base64," + file.getBase64Data())
                .collect(Collectors.toList());

        news.update(requestDto.getTitle(), requestDto.getContent(), base64Images);

        return news.toResponseDto();
    }

    // 뉴스 삭제
    @Transactional
    public void deleteNews(Long id) {
        NewsEntity news = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        newsRepository.delete(news);
    }

}
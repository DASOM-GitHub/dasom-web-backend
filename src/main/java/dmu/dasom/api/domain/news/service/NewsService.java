package dmu.dasom.api.domain.news.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.news.dto.NewsRequestDto;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import dmu.dasom.api.domain.news.entity.NewsEntity;
import dmu.dasom.api.domain.news.repository.NewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

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
    public NewsResponseDto createNews(NewsRequestDto requestDto) {
        NewsEntity news = NewsEntity.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imageUrl(requestDto.getImageUrl())
                .build();

        NewsEntity savedNews = newsRepository.save(news);
        return savedNews.toResponseDto();
    }

    // 수정
    @Transactional
    public NewsResponseDto updateNews(Long id, NewsRequestDto requestDto) {
        NewsEntity news = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        news.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl());
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
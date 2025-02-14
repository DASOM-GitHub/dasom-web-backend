package dmu.dasom.api.domain.news.service;

import dmu.dasom.api.domain.news.dto.NewsRequestDto;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import dmu.dasom.api.domain.news.entity.NewsEntity;
import dmu.dasom.api.domain.news.repository.NewsRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<NewsResponseDto> getAllNews() {
        return newsRepository.findAll().stream()
                .map(news -> new NewsResponseDto(news.getId(), news.getTitle(), news.getContent(), news.getCreatedAt(), news.getImageUrl()))
                .collect(Collectors.toList());
    }

    public NewsResponseDto createNews(NewsRequestDto requestDto) {
        NewsEntity news = new NewsEntity();
        news.setTitle(requestDto.getTitle());
        news.setContent(requestDto.getContent());
        news.setImageUrl(requestDto.getImageUrl());
        news.setCreatedAt(LocalDateTime.now());

        NewsEntity savedNews = newsRepository.save(news);
        return new NewsResponseDto(savedNews.getId(), savedNews.getTitle(), savedNews.getContent(), savedNews.getCreatedAt(), savedNews.getImageUrl());
    }

}

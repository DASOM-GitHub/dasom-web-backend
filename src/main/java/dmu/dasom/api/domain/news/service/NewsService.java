package dmu.dasom.api.domain.news.service;

import dmu.dasom.api.domain.news.dto.NewsRequestDto;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import dmu.dasom.api.domain.news.entity.NewsEntity;
import dmu.dasom.api.domain.news.repository.NewsRepository;
import org.springframework.stereotype.Service;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 뉴스가 존재하지 않습니다. ID: " + id));
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
}

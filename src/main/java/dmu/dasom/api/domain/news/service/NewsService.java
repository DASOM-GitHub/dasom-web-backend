package dmu.dasom.api.domain.news.service;

import dmu.dasom.api.domain.news.dto.NewsCreationResponseDto;
import dmu.dasom.api.domain.news.dto.NewsListResponseDto;
import dmu.dasom.api.global.file.dto.FileResponseDto;
import dmu.dasom.api.global.file.enums.FileType;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

    private final NewsRepository newsRepository;
    private final FileService fileService;

    // 전체 뉴스 조회
    public List<NewsListResponseDto> getAllNews() {
        List<NewsEntity> news = newsRepository.findAll();

        List<Long> newsIds = news.stream()
            .map(NewsEntity::getId)
            .toList();

        Map<Long, FileResponseDto> firstFileMap = fileService.getFirstFileByTypeAndTargetIds(
            FileType.NEWS,
            newsIds
        );

        return news.stream()
            .map(newsEntity -> newsEntity.toListResponseDto(firstFileMap.get(newsEntity.getId())))
            .toList();
    }

    // 개별 뉴스 조회
    public NewsResponseDto getNewsById(Long id) {
        NewsEntity news = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        return news.toResponseDto(fileService.getFilesByTypeAndTargetId(FileType.NEWS, id));
    }

    // 뉴스 생성 (생성된 뉴스 ID 반환)
    @Transactional
    public NewsCreationResponseDto createNews(NewsRequestDto requestDto) {
        return new NewsCreationResponseDto(newsRepository.save(requestDto.toEntity()).getId());
    }

    // 뉴스 수정
    @Transactional
    public NewsResponseDto updateNews(Long id, NewsRequestDto requestDto) {
        NewsEntity news = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        news.update(requestDto.getTitle(), requestDto.getContent());

        return news.toResponseDto();
    }

    // 뉴스 삭제
    @Transactional
    public void deleteNews(Long id) {
        NewsEntity news = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        fileService.deleteFilesByTypeAndTargetId(FileType.NEWS, news.getId());
        newsRepository.delete(news);
    }

}
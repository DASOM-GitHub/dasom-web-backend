package dmu.dasom.api.domain.news.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsImageService {

    // 애플리케이션 실행 경로 기반으로 이미지 저장
    private static final String IMAGE_DIR = System.getProperty("user.dir") + "/uploaded-images/";

    // 애플리케이션 실행 시 이미지 폴더 미리 생성
    @PostConstruct
    public void init() {
        File uploadDir = new File(IMAGE_DIR);
        if (!uploadDir.exists()) {
            boolean isCreated = uploadDir.mkdirs();
            if (isCreated) {
                System.out.println("이미지 저장 폴더 생성됨: " + IMAGE_DIR);
            } else {
                System.err.println("이미지 저장 폴더 생성 실패함.......");
            }
        }
    }

    public List<String> uploadImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();

        if (images != null) {
            for (MultipartFile image : images) {
                try {
                    String imageUrl = saveImage(image);
                    imageUrls.add(imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("이미지 저장 중 오류: " + e.getMessage(), e);
                }
            }
        }

        return imageUrls;
    }

    private String saveImage(MultipartFile image) throws IOException {
        Files.createDirectories(Paths.get(IMAGE_DIR));

        // 원본 파일명 가져오기 (null값 방지용)
        String originalFileName = image.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            originalFileName = "default.png";
        }

        // UUID + 원본 파일명으로 저장
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
        File destFile = new File(IMAGE_DIR + fileName);

        // 파일 저장
        image.transferTo(destFile);

        return "/uploaded-images/" + fileName; // 저장된 이미지 URL 반환
    }

}
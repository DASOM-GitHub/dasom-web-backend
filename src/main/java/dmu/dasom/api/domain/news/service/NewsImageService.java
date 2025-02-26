package dmu.dasom.api.domain.news.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsImageService {

    private static final String IMAGE_DIR = "src/main/resources/static/images/";

    public List<String> uploadImages(List<MultipartFile> images) {

        List<String> imageUrls = new ArrayList<>();

        if (images != null) {
            for (MultipartFile image : images) {
                try {
                    String imageUrl = saveImage(image);
                    imageUrls.add(imageUrl);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 저장 중 오류", e);
                }
            }
        }

        return imageUrls;

    }

    private String saveImage(MultipartFile image) throws IOException {

        File uploadDir = new File(IMAGE_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        File destFile = new File(IMAGE_DIR + fileName);
        image.transferTo(destFile);

        return "/images/" + fileName; // 저장된 이미지 URL 반환
    }

}

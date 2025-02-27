package dmu.dasom.api.global.file.service;

import dmu.dasom.api.global.file.entity.FileEntity;
import dmu.dasom.api.global.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    //파일은 db로 저장하고 일단 물리적으로 uploads/ 에 저장되게
    private static final String FILE_DIR = "uploads/";

    public List<Long> uploadFiles(List<MultipartFile> files) {
        return files.stream()
                .map(this::storeFile)
                .map(fileRepository::save)
                .map(FileEntity::getId)
                .collect(Collectors.toList());
    }

    private FileEntity storeFile(MultipartFile file) {
        try {
            File uploadDir = new File(FILE_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            Path filePath = Path.of(FILE_DIR + storedFilename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return FileEntity.builder()
                    .originalName(originalFilename)
                    .storedName(storedFilename)
                    .filePath(filePath.toString())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public List<FileEntity> getFilesByIds(List<Long> fileIds) {
        return fileRepository.findAllById(fileIds);
    }

}
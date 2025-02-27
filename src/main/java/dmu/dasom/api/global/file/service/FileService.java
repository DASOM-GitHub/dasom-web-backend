package dmu.dasom.api.global.file.service;

import dmu.dasom.api.global.file.dto.FileResponseDto;
import dmu.dasom.api.global.file.entity.FileEntity;
import dmu.dasom.api.global.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public List<Long> uploadFiles(List<MultipartFile> files) {
        List<FileEntity> savedFiles = files.stream().map(file -> {
            try {
                byte[] bytes = file.getBytes();
                String base64Encoded = Base64.getEncoder().encodeToString(bytes);
                return FileEntity.builder()
                        .originalName(file.getOriginalFilename())
                        .base64Data(base64Encoded)
                        .fileType(file.getContentType())
                        .fileSize(file.getSize())
                        .build();
            } catch (IOException e) {
                throw new RuntimeException("파일 인코딩 실패", e);
            }
        }).collect(Collectors.toList());

        return fileRepository.saveAll(savedFiles)
                .stream()
                .map(FileEntity::getId)
                .collect(Collectors.toList());
    }

    // 파일 하나 조회
    public FileResponseDto getFileById(Long fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없음"));

        return convertToDto(file);
    }

    // 파일 여러개 조회
    public List<FileResponseDto> getFilesByIds(List<Long> fileIds) {
        List<FileEntity> files = fileRepository.findAllById(fileIds);
        return files.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // FileEntity → FileResponseDto 변환용
    private FileResponseDto convertToDto(FileEntity file) {
        return FileResponseDto.builder()
                .id(file.getId())
                .fileType(file.getFileType())
                .base64Data("data:" + file.getFileType() + ";base64," + file.getBase64Data())
                .build();
    }

}
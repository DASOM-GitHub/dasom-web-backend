package dmu.dasom.api.global.file.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.global.file.dto.FileResponseDto;
import dmu.dasom.api.global.file.entity.FileEntity;
import dmu.dasom.api.global.file.enums.FileType;
import dmu.dasom.api.global.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    // 파일 업로드
    public void uploadFiles(List<MultipartFile> files, FileType fileType, Long targetId) {
        List<FileEntity> filesToEntity = files.stream()
            .map(file -> FileEntity.builder()
                .originalName(file.getOriginalFilename())
                .encodedData(encode(file))
                .fileFormat(file.getContentType())
                .fileSize(file.getSize())
                .fileType(fileType)
                .targetId(targetId)
                .build())
            .toList();

        fileRepository.saveAllAndFlush(filesToEntity);
    }

    // 단일 파일 조회
    public FileResponseDto getFileById(Long fileId) {
        FileEntity file = fileRepository.findById(fileId)
            .orElseThrow(() -> new CustomException(ErrorCode.EMPTY_RESULT));

        return file.toResponseDto();
    }

    // 파일 타입과 타겟 아이디로 파일 목록 조회
    public List<FileResponseDto> getFilesByTypeAndTargetId(FileType fileType, Long targetId) {
        return findByFileTypeAndTargetId(fileType, targetId)
            .stream()
            .map(FileEntity::toResponseDto)
            .toList();
    }

    public void deleteFilesByTypeAndTargetId(FileType fileType, Long targetId) {
        List<FileEntity> files = findByFileTypeAndTargetId(fileType, targetId);

        if (ObjectUtils.isNotEmpty(files))
            fileRepository.deleteAll(files);
    }

    public Map<Long, FileResponseDto> getFirstFileByTypeAndTargetIds(FileType fileType, List<Long> targetIds) {
        if (targetIds.isEmpty()) {
            return Map.of();
        }

        List<FileEntity> firstFiles = fileRepository.findFirstFilesByTypeAndTargetIds(fileType, targetIds);

        return firstFiles.stream()
            .collect(Collectors.toMap(
                FileEntity::getTargetId,
                FileEntity::toResponseDto,
                (existing, replacement) -> existing
            ));
    }

    private List<FileEntity> findByFileTypeAndTargetId(FileType fileType, Long targetId) {
        return fileRepository.findByFileTypeAndTargetId(fileType, targetId);
    }

    private String encode(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            return Base64.getEncoder()
                .encodeToString(bytes);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_ENCODE_FAIL);
        }
    }

}
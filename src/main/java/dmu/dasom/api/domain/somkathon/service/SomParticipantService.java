package dmu.dasom.api.domain.somkathon.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.somkathon.dto.SomParticipantRequestDto;
import dmu.dasom.api.domain.somkathon.dto.SomParticipantResponseDto;
import dmu.dasom.api.domain.somkathon.entity.SomParticipant;
import dmu.dasom.api.domain.somkathon.repository.SomParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SomParticipantService {
    private final SomParticipantRepository somParticipantRepository;

    // 참가자 등록
    public SomParticipantResponseDto createParticipant(SomParticipantRequestDto requestDto) {
        if(somParticipantRepository.findByStudentId(requestDto.getStudentId()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_STUDENT_NO);
        }

        SomParticipant participant = SomParticipant.builder()
                .participantName(requestDto.getParticipantName())
                .studentId(requestDto.getStudentId())
                .department(requestDto.getDepartment())
                .grade(requestDto.getGrade())
                .contact(requestDto.getContact())
                .email(requestDto.getEmail())
                .build();

        SomParticipant saved = somParticipantRepository.save(participant);

        return toResponseDto(saved);
    }

    /**
     * 모든 참가자 조회 (Read)
     */
    public List<SomParticipantResponseDto> getAllParticipants() {
        return somParticipantRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 특정 참가자 조회 (Read)
     */
    public SomParticipantResponseDto getParticipant(Long id){
        SomParticipant participant = somParticipantRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARTICIPANT));

        return toResponseDto(participant);
    }

    public SomParticipantResponseDto updateParticipant(Long id, SomParticipantRequestDto requestDto){
        SomParticipant participant = somParticipantRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARTICIPANT));

        participant = SomParticipant.builder()
                .id(participant.getId())
                .participantName(requestDto.getParticipantName())
                .studentId(requestDto.getStudentId())
                .department(requestDto.getDepartment())
                .grade(requestDto.getGrade())
                .contact(requestDto.getContact())
                .email(requestDto.getEmail())
                .build();

        SomParticipant updated = somParticipantRepository.save(participant);

        return toResponseDto(updated);
    }

    /**
     * 참가자 삭제 (Delete)
     */
    public void deleteParticipant(Long id) {
        if (!somParticipantRepository.existsById(id)) {
            throw new RuntimeException("참가자를 찾을 수 없습니다.");
        }
        somParticipantRepository.deleteById(id);
    }


    /**
     * Entity → Response DTO 변환 메서드
     */
    private SomParticipantResponseDto toResponseDto(SomParticipant participant) {
        return SomParticipantResponseDto.builder()
                .id(participant.getId())
                .participantName(participant.getParticipantName())
                .studentId(participant.getStudentId())
                .department(participant.getDepartment())
                .grade(participant.getGrade())
                .contact(participant.getContact())
                .email(participant.getEmail())
                .build();
    }
}

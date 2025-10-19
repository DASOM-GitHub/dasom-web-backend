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

        validateDuplicatedStudentId(requestDto.getStudentId());

        SomParticipant participant = SomParticipant.builder()
                .participantName(requestDto.getParticipantName())
                .studentId(requestDto.getStudentId())
                .department(requestDto.getDepartment())
                .grade(requestDto.getGrade())
                .contact(requestDto.getContact())
                .email(requestDto.getEmail())
                .githubLink(requestDto.getGithubLink())
                .portfolioLink(requestDto.getPortfolioLink())
                .build();

        SomParticipant saved = somParticipantRepository.save(participant);

        return saved.toResponseDto(saved);
    }

    /**
     * 모든 참가자 조회 (Read)
     */
    public List<SomParticipantResponseDto> getAllParticipants() {
        return somParticipantRepository.findAll().stream()
                .map(p -> p.toResponseDto(p))
                .collect(Collectors.toList());
    }

    /**
     * 특정 참가자 조회 (Read)
     */
    public SomParticipantResponseDto getParticipant(Long id){
        SomParticipant participant = findParticipantById(id);

        return participant.toResponseDto(participant);
    }

    /**
     * 참가자 수정 (Put)
     */
    public SomParticipantResponseDto updateParticipant(Long id, SomParticipantRequestDto requestDto){
        SomParticipant participant = findParticipantById(id);

        participant.update(requestDto);

        return participant.toResponseDto(participant);
    }

    /**
     * 참가자 삭제 (Delete)
     */
    public void deleteParticipant(Long id) {
        findParticipantById(id);
        somParticipantRepository.deleteById(id);
    }

    /**
     * ID로 참가자 조회 (공통 처리)
     */
    private SomParticipant findParticipantById(Long id) {
        return somParticipantRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARTICIPANT));
    }

    private void validateDuplicatedStudentId(String studentId) {
        if (somParticipantRepository.findByStudentId(studentId).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_STUDENT_NO);
        }
    }
}

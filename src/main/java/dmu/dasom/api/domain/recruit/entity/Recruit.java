package dmu.dasom.api.domain.recruit.entity;

import dmu.dasom.api.domain.recruit.dto.RecruitConfigResponseDto;
import dmu.dasom.api.domain.recruit.enums.ConfigKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Builder
@DynamicUpdate
@Entity
@Getter
@NoArgsConstructor
public class Recruit {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Enumerated(EnumType.STRING)
    @Column(name = "key", length = 64, nullable = false, unique = true)
    @Id
    private ConfigKey key;

    @Column(name = "value")
    private String value;

    public void updateDateTime(final LocalDateTime dateTime) {
        this.value = dateTime.format(DATE_TIME_FORMATTER);
    }

    public void updateTime(final LocalTime time) {
        this.value = time.format(TIME_FORMATTER);
    }


    // 기수 업데이트
    public void updateGeneration(final String generation) {
        this.value = generation; // ex. "34기"
    }

    public RecruitConfigResponseDto toResponse() {
        if(this.key == ConfigKey.GENERATION){
            return RecruitConfigResponseDto.builder()
                    .key(key)
                    .value(value)
                    .build();
        }
        LocalDateTime dateTime = LocalDateTime.parse(this.value, DATE_TIME_FORMATTER);
        return RecruitConfigResponseDto.builder()
            .key(key)
            .value(dateTime.format(DATE_TIME_FORMATTER))
            .build();
    }

    public RecruitConfigResponseDto toTimeResponse() {
        LocalTime time = LocalTime.parse(this.value, TIME_FORMATTER);
        return RecruitConfigResponseDto.builder()
            .key(key)
            .value(time.format(TIME_FORMATTER))
            .build();
    }

}

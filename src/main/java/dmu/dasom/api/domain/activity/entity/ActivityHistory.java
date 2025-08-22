package dmu.dasom.api.domain.activity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int year;
    private String section;
    private String title;
    private String award;

    public void update(int year, String section, String title, String award) {
        this.year = year;
        this.section = section;
        this.title = title;
        this.award = award;
    }
}

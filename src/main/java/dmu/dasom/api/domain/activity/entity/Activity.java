package dmu.dasom.api.domain.activity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(nullable = false)
    private LocalDate activityDate;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 50)
    private String award;

    public static Activity create(Section section, LocalDate activityDate, String title, String award) {
        return Activity.builder()
                .section(section)
                .activityDate(activityDate)
                .title(title)
                .award(award)
                .build();
    }

    public void update(Section section, LocalDate activityDate, String title, String award) {
        this.section = section;
        this.activityDate = activityDate;
        this.title = title;
        this.award = award;
    }
}

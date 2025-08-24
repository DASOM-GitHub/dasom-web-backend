package dmu.dasom.api.global.generation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@AllArgsConstructor
@Builder
@DynamicUpdate
@Entity
@Getter
@NoArgsConstructor
public class Generation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    @Column(nullable = false, unique = true, length = 10)
    private String generation; // 기수 (예: "1기", "2기")


    public void updateGeneration(String generation) {
        this.generation = generation;
    }
}

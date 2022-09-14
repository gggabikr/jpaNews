package jpanews.jpaproject1.domain.Words;

import jpanews.jpaproject1.domain.WordClass;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "language")
public abstract class Word {

    @Id @GeneratedValue
    @Column(name="word_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private WordClass wordClass;
}

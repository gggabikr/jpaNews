package jpanews.jpaproject1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Word {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Language langType;

    private WordClass wordClass;

    @ManyToOne
    private WordListToWord word;

    private String meaning;


}

package jpanews.jpaproject1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class WordListToWord {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private WordList wordList;

    private Word word;

    private LocalDateTime addDate;

}

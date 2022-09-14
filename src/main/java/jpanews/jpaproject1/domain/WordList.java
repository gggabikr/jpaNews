package jpanews.jpaproject1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class WordList {

    @Id @GeneratedValue
    @Column(name="wordlist_id")
    private Long id;

    @ManyToOne
    private Member member;

    @OneToMany
    private List<WordListToWord> wordListToWords = new ArrayList<>();
}

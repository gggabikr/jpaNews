package jpanews.jpaproject1.domain;

import jpanews.jpaproject1.domain.Words.Word;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WordListToWord {

    @Id @GeneratedValue
    @Column(name="ww_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wordlist_id")
    private WordList wordList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    private LocalDateTime addDate;

    private boolean status; //[1 = Active, 0 = Inactive]

    private int failedCount; //틀린 횟수

    private int testedCount; //테스트 횟수


    //==Constructor==//
    public static WordListToWord createWordListToWord(Word word){
        WordListToWord wordListToWord = new WordListToWord();

        wordListToWord.setWord(word);
        wordListToWord.setAddDate(LocalDateTime.now());
        wordListToWord.setStatus(true);
        wordListToWord.setFailedCount(0);
        wordListToWord.setTestedCount(0);

        return wordListToWord;
    }

    public void updateDate(){
        this.setAddDate(LocalDateTime.now());
    }

    public void updateStatus(){
        this.status = !this.status;
    }


    public void delete() {
        this.wordList.getWordListToWords().remove(this);
    }
}

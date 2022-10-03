package jpanews.jpaproject1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class WordList {

    @Id @GeneratedValue
    @Column(name="wordlist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "wordList", cascade = CascadeType.ALL)
    private List<WordListToWord> wordListToWords = new ArrayList<>();

    //finish rate

    private int numerator; //분자

    private int denominator; //분모


    //==relational methods==//
    public void setMember(Member member){
        this.member = member;
        member.getWordLists().add(this);
    }

    public void saveWordListToWord(WordListToWord wordListToWord){
        this.getWordListToWords().add(wordListToWord);
        wordListToWord.setWordList(this);
    }


    //constructor
    public static WordList createWordList(Member member, WordListToWord... wordListToWords){
        WordList wordList = new WordList();
        wordList.setMember(member);

        for(WordListToWord wordListToWord : wordListToWords){
            wordList.saveWordListToWord(wordListToWord);
        }
        wordList.denominator = wordList.getWordListToWords().size();
        wordList.numerator = 0;
        return wordList;
    }

    public void deleteWordList(){
        for (WordListToWord wordListToWord : this.wordListToWords) {
            wordListToWord.delete();
        }

    }
}

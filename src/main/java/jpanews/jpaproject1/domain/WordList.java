package jpanews.jpaproject1.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WordList {

    @Id @GeneratedValue
    @Column(name="wordlist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "wordList", cascade = CascadeType.ALL)
    private List<WordListToWord> wordListToWords = new ArrayList<>();

    //memorized rate
    private int numerator; //분자

    private int denominator; //분모


//    public void updateTestResults(String testResults) {
//        for (int i = 0; i < testResults.length(); i++) {
//            wordListToWords.get(i).updateTestResult(testResults.charAt(i));
//        }
////        for (WordListToWord wordListToWord:wordListToWords) {
////            wordListToWord.updateRecentTest();
////        }
//    }

    public void updateMemorizedStatus(){
        int temp = 0;
        for(WordListToWord wordListToWord : getWordListToWords()){
            if(wordListToWord.getStatus()){
                temp++;
            }
        }
        this.numerator =temp;
        this.denominator = getWordListToWords().size();
    }

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
    public static WordList createWordList(Member member, WordListToWord... wlws){
        WordList wordList = new WordList();
        wordList.setMember(member);

        if(wlws.length>0){
            for(WordListToWord wordListToWord : wlws){
                wordList.saveWordListToWord(wordListToWord);
            }
            wordList.denominator = wordList.getWordListToWords().size();
        } else{
            wordList.denominator = 0;
        }
        wordList.numerator = 0;
        return wordList;
    }

//    public static WordList createWordList(Member member){
//        WordList wordList = new WordList();
//        wordList.setMember(member);
//
//        wordList.denominator = 0;
//        wordList.numerator = 0;
//        return wordList;
//    }
}

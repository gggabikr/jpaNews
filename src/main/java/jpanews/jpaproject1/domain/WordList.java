package jpanews.jpaproject1.domain;

import lombok.Getter;

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

    //memorized rate
    private int numerator; //분자

    private int denominator; //분모

    public void updateTestResults(String testResults) {
        for (int i = 0; i < testResults.length(); i++) {
            wordListToWords.get(i).updateRecentTest(testResults.charAt(i));
        }
//        for (WordListToWord wordListToWord:wordListToWords) {
//            wordListToWord.updateRecentTest();
//        }
    }

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

//    public int[] testWords(int numOfWords){
//
//    }
}

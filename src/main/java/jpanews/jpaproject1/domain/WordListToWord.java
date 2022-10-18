package jpanews.jpaproject1.domain;

import jpanews.jpaproject1.domain.Words.Word;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    private Long addDate;

    private boolean status; //[1 = Active, 0 = Inactive]

    private int failedCount; //틀린 횟수

    private int testedCount; //테스트 횟수

    private String recentTest; //recent 10 test result

    //==Constructor==//
    public static List<WordListToWord> createWordListToWord(Word... words){

        ArrayList<WordListToWord> wlws = new ArrayList<>();
        for(Word word: words){
        WordListToWord wordListToWord = new WordListToWord();
        wordListToWord.setWord(word);
        wordListToWord.setAddDate(Timestamp.valueOf(LocalDateTime.now()).getTime());
        wordListToWord.setStatus(true);
        wordListToWord.setFailedCount(0);
        wordListToWord.setTestedCount(0);
        wordListToWord.recentTest = "";
        //don't set WordList here yet
         wlws.add(wordListToWord);
        }
        return wlws;
    }


//    public void updateDate(){
//        this.setAddDate(LocalDateTime.now());
//    }


    public boolean getStatus(){
        return this.status;
    }

    public void updateStatus(){
        this.status = !this.status;
        this.wordList.updateMemorizedStatus();
    }

    public void resetTestResults(){
        this.setFailedCount(0);
        this.setFailedCount(0);
        this.setRecentTest("");
    }

    public float getCorrectAnswerRate(){
        if(testedCount ==0){
            return 0;
        }
        return (float)(testedCount-failedCount)/testedCount;
    }

    public void updateTestResult(int a){
        String result = String.valueOf(a);
        if (getRecentTest().length() >= 10) {
            setRecentTest(getRecentTest().substring(1));
        }
        setRecentTest(getRecentTest()+result);

        if (a == 0){
            failedCount++;
        }
        testedCount++;
    }
//    Not needed
//    public void delete() {
//        getWordList().getWordListToWords().remove(this);
//    }


}

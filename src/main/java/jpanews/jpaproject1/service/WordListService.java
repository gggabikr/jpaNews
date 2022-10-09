package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.WordList;
import jpanews.jpaproject1.domain.WordListToWord;
import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.MemberRepository;
import jpanews.jpaproject1.repository.WordListRepository;
import jpanews.jpaproject1.repository.WordListToWordRepository;
import jpanews.jpaproject1.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WordListService {
    private final WordRepository wordRepository;
    private final WordListRepository wordListRepository;
    private final WordListToWordRepository wlwRepository;
    private final MemberRepository memberRepository;

    //==create WordList==//
    @Transactional
    public Long createWordList(Long memberId, List<Long> wordIdList) {

        //Refer entities
        Member member = memberRepository.findOne(memberId);

        List<WordListToWord> wlws = new ArrayList<>();
        for(Long wordId : wordIdList){
            Word word = wordRepository.findOne(wordId);
            WordListToWord wlw = WordListToWord.createWordListToWord(word);
            wlws.add(wlw);
        }
        WordList newWordList = WordList.createNewWordList(member, wlws);

        return newWordList.getId();
    }

    //==delete WordList==//
    @Transactional
    public void deleteWordList(Long wordListId){
        WordList wordList = wordListRepository.findOne(wordListId);
        for(WordListToWord wlw: wordList.getWordListToWords()){
            wlw.delete();
        }
    }

    //==test==//
    public int testOneWord(WordListToWord wlw) throws Exception {
        //여기에서 단어만 하나 받아서 answers를 만들고 순서 섞고 해서 문제 낸다음
        List<String> answers = new ArrayList<>();
        String rightAnswer = wlw.getWord().getMeaning();
        answers.add(rightAnswer);
        List<Word> byWordClass = wordRepository.findByWordClass(String.valueOf(wlw.getWord().getWordClass()));
        for (int i = 0; i<3; i++){
            String wrongAnswer = byWordClass.get((int) (Math.random() * byWordClass.size())).getMeaning();
            answers.add(wrongAnswer);
        }
        Collections.shuffle(answers);

        //유저인풋 받는거 고민해보기
        int indexOfRightAnswer = answers.indexOf(rightAnswer);
        int OX; //right answer = 1, wrong answer = 0
        int userAnswer = 999; //choice of 0~3
        if (userAnswer==indexOfRightAnswer){
            return 1;
        } else {return 0;}
        //정답여부를 0 과 1로 리턴하는 것까지 하기.
        //그리고 아래 메서드에서 리턴된 숫자와 랜덤으로 뽑힌 단어들을 가지고 결과기록하는 방식으로 진행.
    }


    @Transactional
    public String testWords(Long wordListId,int numOfWords) throws Exception {

        List<WordListToWord> randomSelectedWlws
                = wlwRepository.RandomSelect(wordListId, numOfWords);

        HashMap<Integer, List<String>> hashMapOfTestQ = new HashMap<>();

        for (WordListToWord wlw : randomSelectedWlws) {

            hashMapOfTestQ.put(answers.indexOf(rightAnswer),answers);
        }

    }
}

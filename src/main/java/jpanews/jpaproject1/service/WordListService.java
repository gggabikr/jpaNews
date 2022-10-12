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
    public List<String> makeAnswerList(WordListToWord wlw) throws Exception {
        List<String> answers = new ArrayList<>();
        String rightAnswer = wlw.getWord().getMeaning();
        answers.add(rightAnswer);
        List<Word> byWordClass = wordRepository.findByWordClass(String.valueOf(wlw.getWord().getWordClass()));
        for (int i = 0; i<3; i++){
            String wrongAnswer = byWordClass.get((int) (Math.random() * byWordClass.size())).getMeaning();
            answers.add(wrongAnswer);
        }
        Collections.shuffle(answers);
        return answers;
    }

    public int checkRightOrWrong(WordListToWord rightAnswer, int userInput) throws Exception {
        List<String> answerList = makeAnswerList(rightAnswer);
        int indexOfRightAnswer = answerList.indexOf(rightAnswer.getWord().getMeaning());
        if (userInput==indexOfRightAnswer){
            return 1;
        } else {return 0;}
    }


    @Transactional
    public void testWords(Long wordListId,int numOfWords) throws Exception {

        List<WordListToWord> randomSelectedWlws
                = wlwRepository.RandomSelect(wordListId, numOfWords);

        StringBuilder OxList = new StringBuilder();
//        HashMap<Integer, List<String>> hashMapOfTestQ = new HashMap<>();

        for (WordListToWord wlw : randomSelectedWlws) {
            //need to be replaced with actual user input//
            int userInput = 999;
            int OX = checkRightOrWrong(wlw, userInput);
            OxList.append(OX);
        }

        for(int i=0; i< randomSelectedWlws.size(); i++){
            randomSelectedWlws.get(i).updateTestResult(OxList.charAt(i));
        }
    }
}

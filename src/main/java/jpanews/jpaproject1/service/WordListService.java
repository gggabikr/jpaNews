package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.WordList;
import jpanews.jpaproject1.domain.WordListToWord;
import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.*;
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
    private final CustomWordListToWordRepositoryImpl wordListToWordRepository;
    private final MemberRepository memberRepository;

    // ==create WordList==//
    @Transactional
    public Long createWordList(Long memberId, Word... words) {

        // Refer entities
        Member member = memberRepository.findOne(memberId);

        if (words.length > 0) {
            List<WordListToWord> wlws = WordListToWord.createWordListToWord(words);
            WordList newWordList = WordList.createWordList(member, wlws.toArray(new WordListToWord[0]));
            newWordList.changeWordListName(wordListNaming(memberId));
            wordListRepository.save(newWordList);
            return newWordList.getId();
        } else {
            WordList newWordList = WordList.createWordList(member);
            newWordList.changeWordListName(wordListNaming(memberId));
            wordListRepository.save(newWordList);
            return newWordList.getId();
        }
    }

    // ==WordList naming==//
    public String wordListNaming(Long memberId) {

        for (int i = 1; i < 1000; i++) {
            String j;
            if (i < 10) {
                j = "0" + i;
            } else {
                j = String.valueOf(i);
            }
            String wordListName = "Unnamed List" + j;
            if (wordListRepository.findOneByWordListNameAndMemberId(wordListName, memberId).size() == 0) {
                return wordListName;
            }
        }
        return "NoMoreNameAvailable";
    }

    @Transactional
    public Long changeWordListName(Long wordListId, String wordListName) {
        wordListRepository.changeWordListName(wordListId, wordListName);
        return wordListId;
    }

    // ==delete WordList==//
    @Transactional
    public void deleteWordList(Long wordListId) {
        List<WordListToWord> allWlwByWordList = wordListToWordRepository.findAllByWordList(wordListId);
        for (WordListToWord wlw : allWlwByWordList) {
            wordListToWordRepository.deleteWlw(wlw.getId());
        }
        wordListRepository.deleteWordList(wordListId);
    }

    public WordList findOneWordList(Long wordListId) {
        return wordListRepository.findOne(wordListId);
    }

    public List<WordList> findAllWordListByMember(Long memberId) {
        return wordListRepository.findAllByMember(memberId);
    }

    public List<WordList> findAllByWordListWithMemorizedStatus(Long memberId, int percent) {
        return wordListRepository.findAllByMemorizedStatus(memberId, percent);
    }

    // add & delete wlw from the wordlist
    @Transactional
    public Long addWordsToWordList(Long wordListId, Word... words) {
        WordList wordList = wordListRepository.findOne(wordListId);
        List<WordListToWord> wordListToWords = WordListToWord.createWordListToWord(words);
        System.out.println(wordListToWords);
        for (WordListToWord wlw : wordListToWords) {
            wordList.saveWordListToWord(wlw);
            wordListToWordRepository.createWlw(wlw);
        }
        wordList.updateMemorizedStatus();
        return wordListId;
    }

    @Transactional
    public Long deleteWordsFromWordList(Long wordListId, WordListToWord... wordListToWords) {
        WordList wordList = wordListRepository.findOne(wordListId);
        for (WordListToWord wlw : wordListToWords) {
            wordList.getWordListToWords().remove(wlw);
            wordListToWordRepository.deleteWlw(wlw.getId());
        }
        wordList.updateMemorizedStatus();
        return wordListId;
    }

    @Transactional
    public Long deleteWordsFromWordListWithIds(Long wordListId, Long... wordIds) {
        WordList wordList = wordListRepository.findOne(wordListId);
        for (Long wordId : wordIds) {
            wordList.getWordListToWords()
                    .remove(wordListToWordRepository.findByWordIdAndWordListId(wordListId, wordId).get(0));
            wordListToWordRepository.deleteWlw(wordListId, wordId);
        }
        wordList.updateMemorizedStatus();
        return wordListId;
    }

    /*
     * //==test==//
     */


    public testQuestionObj makeAnswerList(WordListToWord wlw) throws Exception {
        List<String> answers = new ArrayList<>();
        String rightAnswer = wlw.getWord().getMeaning();
        answers.add(rightAnswer);
        List<Word> byWordClass = wordRepository.findByWordClass(String.valueOf(wlw.getWord().getWordClass()));
        for (int i = 0; i < 3; i++) {
            String wrongAnswer = byWordClass.get((int) (Math.random() * byWordClass.size())).getMeaning();
            if (!answers.contains(wrongAnswer))
                answers.add(wrongAnswer);
        }
        // in the case if there's not enough word data in DB
        if (answers.size() < 4) {
            List<Word> allWords = wordRepository.findAll();
            while (answers.size() < 4) {
                String wrongAnswer = allWords.get((int) (Math.random() * allWords.size())).getMeaning();
                if (!answers.contains(wrongAnswer))
                    answers.add(wrongAnswer);
            }
        }

        Collections.shuffle(answers);
        // System.out.println("단어: "+ wlw.getWord().getName());
        // System.out.println("선택지: "+ answers);
        testQuestionObj testObj = new testQuestionObj();
        testObj.setWlw(wlw);
        testObj.setAnswerList(answers);
        testObj.setIndexOfCorrectAns(answers.indexOf(wlw.getWord().getMeaning()));
        return testObj;
    }


    public int checkRightOrWrong(testQuestionObj obj) {
        if (obj.getIndexOfCorrectAns()==obj.getIndexOfUserInput()){
            return 1;
        } else {return 0;}
    }

    @Transactional
    public void testWords(ArrayList<testQuestionObj> Objs) throws Exception {
        testWords_code_fragment(Objs);
    }

    // Test random selected words
    @Transactional
    public void testWords(Long wordListId, int numOfWords) throws Exception {

        List<WordListToWord> randomSelectedWlws = wordListToWordRepository.RandomSelect(wordListId, numOfWords);

        ArrayList<testQuestionObj> testObjs = new ArrayList<>();
        for (WordListToWord wlw : randomSelectedWlws) {
            testObjs.add(makeAnswerList(wlw));
        }
        // ??? 이렇게 되면 유저 인풋을 받는 단계가 어려워진다. 컨트롤러에서 만들고 유저인풋까지 받아와서 여기서 처리하도록 다시 만들자.
    }

    @Transactional
    public Long resetTestResult(Long wlwId) {
        return wordListToWordRepository.findOne(wlwId).resetTestResults();
    }

    @Transactional
    public Long toggleStatus(Long wlwId) {
        WordListToWord wlw = wordListToWordRepository.findOne(wlwId);
        wlw.updateStatus();
        return wlw.getWordList().getId();
    }

    @Transactional
    public void testWords_code_fragment(ArrayList<testQuestionObj> ObjList) {
        StringBuilder OxList = new StringBuilder();

        ArrayList<WordListToWord> wlws = new ArrayList<>();
        for(testQuestionObj obj: ObjList){
            wlws.add(obj.getWlw());
        }

        for (jpanews.jpaproject1.service.testQuestionObj testQuestionObj : ObjList) {
            int OX = checkRightOrWrong(testQuestionObj);
            OxList.append(OX);
        }

        System.out.println("OX list: " + OxList);
//        for (int i = 0; i < ObjList.size(); i++) {
//            ObjList.get(i).getWlw().updateTestResult(Integer.parseInt(String.valueOf(OxList.charAt(i))));
//        }
        wordListRepository.updateTestResult(wlws, String.valueOf(OxList));
    }
}
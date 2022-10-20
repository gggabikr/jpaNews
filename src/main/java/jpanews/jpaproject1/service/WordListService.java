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
import java.util.Scanner;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WordListService {
    private final WordRepository wordRepository;
    private final WordListRepository wordListRepository;
    private final  WordListToWordRepository wordListToWordRepository;
    private final WordListToWordRepository wlwRepository;
    private final MemberRepository memberRepository;

    //==create WordList==//
    @Transactional
    public Long createWordList(Long memberId, Word... words) {

        //Refer entities
        Member member = memberRepository.findOne(memberId);

        if(words.length >0){
            List<WordListToWord> wlws = WordListToWord.createWordListToWord(words);
            WordList newWordList = WordList.createWordList(member, wlws.toArray(new WordListToWord[0]));
            wordListRepository.save(newWordList);
            return newWordList.getId();
        }else {
            WordList newWordList = WordList.createWordList(member);
            wordListRepository.save(newWordList);
            return newWordList.getId();
        }
    }


    //==delete WordList==//
    @Transactional
    public void deleteWordList(Long wordListId){
        List<WordListToWord> allWlwByWordList = wlwRepository.findAllByWordList(wordListId);
        for(WordListToWord wlw: allWlwByWordList){
        wlwRepository.deleteWlw(wlw.getId());
        }
        wordListRepository.deleteWordList(wordListId);
    }

    public WordList findOneWordList(Long wordListId){
        return wordListRepository.findOne(wordListId);
    }

    public List<WordList> findAllWordListByMember(Long memberId){
        return wordListRepository.findAllByMember(memberId);
    }

    public List<WordList> findAllByWordListWithMemorizedStatus(Long memberId, int percent){
        return wordListRepository.findAllByMemorizedStatus(memberId,percent);
    }

    //add & delete wlw from the wordlist
    public Long addWordsToWordList(Long wordListId, Word... words){
        WordList wordList = wordListRepository.findOne(wordListId);
        List<WordListToWord> wordListToWords = WordListToWord.createWordListToWord(words);
        for(WordListToWord wlw: wordListToWords){
            wordList.saveWordListToWord(wlw);
        }
        return wordListId;
    }

    public Long deleteWordsFromWordList(Long wordListId, WordListToWord... wordListToWords){
        WordList wordList = wordListRepository.findOne(wordListId);
        for(WordListToWord wlw: wordListToWords){
//            wordList.getWordListToWords().remove(wlw);
//            wlw.setWordList(null);
            wordListToWordRepository.deleteWlw(wlw.getId());
        }
        return wordListId;
    }



    /*
         //==test==//
    */

    public List<String> makeAnswerList(WordListToWord wlw) throws Exception {
        List<String> answers = new ArrayList<>();
        String rightAnswer = wlw.getWord().getMeaning();
        answers.add(rightAnswer);
        List<Word> byWordClass = wordRepository.findByWordClass(String.valueOf(wlw.getWord().getWordClass()));
        for (int i = 0; i<3; i++){
            String wrongAnswer = byWordClass.get((int) (Math.random() * byWordClass.size())).getMeaning();
            if(!answers.contains(wrongAnswer))
            answers.add(wrongAnswer);
        }
        if(answers.size()<4){
            List<Word> allWords = wordRepository.findAll();
            while(answers.size()<4){
                String wrongAnswer = allWords.get((int) (Math.random() * allWords.size())).getMeaning();
                if(!answers.contains(wrongAnswer))
                    answers.add(wrongAnswer);
            }
        }
        Collections.shuffle(answers);
        System.out.println("단어: "+ wlw.getWord().getName());
        System.out.println("선택지: "+ answers);
        return answers;
    }

    public int checkRightOrWrong(WordListToWord rightAnswer, int userInput) throws Exception {
        List<String> answerList = makeAnswerList(rightAnswer);
        int indexOfRightAnswer = answerList.indexOf(rightAnswer.getWord().getMeaning());
        System.out.println("정답: "+ indexOfRightAnswer);
        System.out.println("유저의 답: "+ userInput);
        if (userInput==indexOfRightAnswer){
            return 1;
        } else {return 0;}
    }


    //Test words for selected words
    @Transactional
    public void testWords(Long wordListId, WordListToWord... wlws) throws Exception {

        List<WordListToWord> randomSelectedWlws = List.of(wlws);
        testWords_code_fragment(randomSelectedWlws);
    }

    //Test random selected words
    @Transactional
    public void testWords(Long wordListId,int numOfWords) throws Exception {

        List<WordListToWord> randomSelectedWlws
                = wlwRepository.RandomSelect(wordListId, numOfWords);

        testWords_code_fragment(randomSelectedWlws);
    }

    private void testWords_code_fragment(List<WordListToWord> randomSelectedWlws) throws Exception {
        StringBuilder OxList = new StringBuilder();

        for (WordListToWord wlw : randomSelectedWlws) {

//            //==for test the method==//
//            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//            System.out.println("Enter the answer");
//            int userInput = myObj.nextInt();  // Read user input

            //need to be replaced with actual user input//
            int userInput = 0;
            System.out.println();
            int OX = checkRightOrWrong(wlw, userInput);
            OxList.append(OX);
        }
        System.out.println(OxList);
        for(int i = 0; i< randomSelectedWlws.size(); i++){
            randomSelectedWlws.get(i).updateTestResult(Integer.parseInt(String.valueOf(OxList.charAt(i))));
        }
    }
}

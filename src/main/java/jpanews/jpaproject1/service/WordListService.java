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

    //==create WordList==//
    @Transactional
    public Long createWordList(Long memberId, Word... words) {

        //Refer entities
        Member member = memberRepository.findOne(memberId);

        if(words.length >0){
            List<WordListToWord> wlws = WordListToWord.createWordListToWord(words);
            WordList newWordList = WordList.createWordList(member, wlws.toArray(new WordListToWord[0]));
            newWordList.changeWordListName(wordListNaming(memberId));
            wordListRepository.save(newWordList);
            return newWordList.getId();
        }else {
            WordList newWordList = WordList.createWordList(member);
            newWordList.changeWordListName(wordListNaming(memberId));
            wordListRepository.save(newWordList);
            return newWordList.getId();
        }
    }

    //==WordList naming==//
    public String wordListNaming(Long memberId){

        for(int i = 1; i<1000; i++) {
            String j;
            if (i < 10) {
                j = "0" + i;
            } else {
                j = String.valueOf(i);
            }
            String wordListName = "Unnamed List" + j;
            if(wordListRepository.findOneByWordListNameAndMemberId(wordListName, memberId).size() == 0){
                return wordListName;
            }
        }
        return "NoMoreNameAvailable";
    }

    public Long changeWordListName(Long wordListId, String wordListName){
        wordListRepository.changeWordListName(wordListId, wordListName);
        return wordListId;
    }


    //==delete WordList==//
    @Transactional
    public void deleteWordList(Long wordListId){
        List<WordListToWord> allWlwByWordList = wordListToWordRepository.findAllByWordList(wordListId);
        for(WordListToWord wlw: allWlwByWordList){
            wordListToWordRepository.deleteWlw(wlw.getId());
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
    @Transactional
    public Long addWordsToWordList(Long wordListId, Word... words){
        WordList wordList = wordListRepository.findOne(wordListId);
        List<WordListToWord> wordListToWords = WordListToWord.createWordListToWord(words);
        System.out.println(wordListToWords);
        for(WordListToWord wlw: wordListToWords){
            wordList.saveWordListToWord(wlw);
            wordListToWordRepository.createWlw(wlw);
        }
        wordList.updateMemorizedStatus();
        return wordListId;
    }
    @Transactional
    public Long deleteWordsFromWordList(Long wordListId, WordListToWord... wordListToWords){
        WordList wordList = wordListRepository.findOne(wordListId);
        for(WordListToWord wlw: wordListToWords){
            wordList.getWordListToWords().remove(wlw);
            wordListToWordRepository.deleteWlw(wlw.getId());
        }
        wordList.updateMemorizedStatus();
        return wordListId;
    }

    @Transactional
    public Long deleteWordsFromWordListWithIds(Long wordListId, Long... wordIds){
        WordList wordList = wordListRepository.findOne(wordListId);
        for(Long wordId: wordIds){
            wordList.getWordListToWords().remove(wordListToWordRepository.findByWordIdAndWordListId(wordListId, wordId).get(0));
            wordListToWordRepository.deleteWlw(wordListId, wordId);
        }
        wordList.updateMemorizedStatus();
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
        //in the case if there's not enough word data in DB
        if(answers.size()<4){
            List<Word> allWords = wordRepository.findAll();
            while(answers.size()<4){
                String wrongAnswer = allWords.get((int) (Math.random() * allWords.size())).getMeaning();
                if(!answers.contains(wrongAnswer))
                    answers.add(wrongAnswer);
            }
        }

        Collections.shuffle(answers);
        System.out.println("??????: "+ wlw.getWord().getName());
        System.out.println("?????????: "+ answers);
        return answers;
    }

    public int checkRightOrWrong(List<String> answerList, String userInput) throws Exception {
//        if(answerList.get(userInput).equals())
        String[] split = userInput.split("S");
        Long wlwId = Long.valueOf(split[0]);
        int userAnswer = Integer.parseInt(split[1]);
        if(answerList.get(userAnswer).equals(wordListToWordRepository.findOne(wlwId).getWord().getMeaning())){
            return 1;
        } else{
            return 0;
        }
    }


    public int checkRightOrWrong(WordListToWord rightAnswer, int userInput) throws Exception {
        List<String> answerList = makeAnswerList(rightAnswer);
        int indexOfRightAnswer = answerList.indexOf(rightAnswer.getWord().getMeaning());
        System.out.println("??????: "+ indexOfRightAnswer);
        System.out.println("????????? ???: "+ userInput);
        if (userInput==indexOfRightAnswer){
            return 1;
        } else {return 0;}
    }


    //Test words for selected words
    @Transactional
    public void testWords(WordListToWord... wlws) throws Exception {

        List<WordListToWord> SelectedWlws = List.of(wlws);
        testWords_code_fragment(SelectedWlws);
    }

    //Test random selected words
    @Transactional
    public void testWords(Long wordListId,int numOfWords) throws Exception {

        List<WordListToWord> randomSelectedWlws
                = wordListToWordRepository.RandomSelect(wordListId, numOfWords);

        testWords_code_fragment(randomSelectedWlws);
    }

    @Transactional
    public Long resetTestResult(Long wlwId){
        return wordListToWordRepository.findOne(wlwId).resetTestResults();
    }

    @Transactional
    public Long toggleStatus(Long wlwId){
        WordListToWord wlw = wordListToWordRepository.findOne(wlwId);
        wlw.updateStatus();
        return wlw.getWordList().getId();
    }

    private void testWords_code_fragment(List<WordListToWord> selectedWlws) throws Exception {
        StringBuilder OxList = new StringBuilder();

        for (WordListToWord wlw : selectedWlws) {

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
        for(int i = 0; i< selectedWlws.size(); i++){
            selectedWlws.get(i).updateTestResult(Integer.parseInt(String.valueOf(OxList.charAt(i))));
        }
    }
}

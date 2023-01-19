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

    public Long deleteWordsFromWordList(Long wordListId, WordListToWord... wordListToWords){
        WordList wordList = wordListRepository.findOne(wordListId);
        for(WordListToWord wlw: wordListToWords){
            wordList.getWordListToWords().remove(wlw);
            wordListToWordRepository.deleteWlw(wlw.getId());
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

<<<<<<< Updated upstream
    public int checkRightOrWrong(WordListToWord rightAnswer, int userInput) throws Exception {
        List<String> answerList = makeAnswerList(rightAnswer);
        int indexOfRightAnswer = answerList.indexOf(rightAnswer.getWord().getMeaning());
        System.out.println("정답: "+ indexOfRightAnswer);
        System.out.println("유저의 답: "+ userInput);
        if (userInput==indexOfRightAnswer){
=======
//    public int checkRightOrWrong(List<String> answerList, String userInput) throws Exception {
////        if(answerList.get(userInput).equals())
//        String[] split = userInput.split("S");
//        Long wlwId = Long.valueOf(split[0]);
//        int userAnswer = Integer.parseInt(split[1]);
//        if(answerList.get(userAnswer).equals(wordListToWordRepository.findOne(wlwId).getWord().getMeaning())){
//            return 1;
//        } else{
//            return 0;
//        }
//    }


//    public int checkRightOrWrong(WordListToWord rightAnswer, int userInput) throws Exception {
//        List<String> answerList = makeAnswerList(rightAnswer);
//        int indexOfRightAnswer = answerList.indexOf(rightAnswer.getWord().getMeaning());
//        System.out.println("정답: "+ indexOfRightAnswer);
//        System.out.println("유저의 답: "+ userInput);
//        if (userInput==indexOfRightAnswer){
//            return 1;
//        } else {return 0;}
//    }

    public int checkRightOrWrong(testQuestionObj obj) {
        if (obj.getIndexOfCorrectAns()==obj.getIndexOfUserInput()){
>>>>>>> Stashed changes
            return 1;
        } else {return 0;}
    }


    //Test words for selected words
<<<<<<< Updated upstream
    @Transactional
    public void testWords(Long wordListId, WordListToWord... wlws) throws Exception {

        List<WordListToWord> randomSelectedWlws = List.of(wlws);
        testWords_code_fragment(randomSelectedWlws);
=======
//    @Transactional
//    public void testWords(WordListToWord... wlws) throws Exception {
//
//        List<WordListToWord> SelectedWlws = List.of(wlws);
//        testWords_code_fragment(SelectedWlws);
//    }

    @Transactional
    public void testWords(ArrayList<testQuestionObj> Objs) throws Exception {
        testWords_code_fragment(Objs);
>>>>>>> Stashed changes
    }


    //Test random selected words
    @Transactional
    public void testWords(Long wordListId,int numOfWords) throws Exception {

        List<WordListToWord> randomSelectedWlws
                = wordListToWordRepository.RandomSelect(wordListId, numOfWords);

        ArrayList<testQuestionObj> testObjs = new ArrayList<>();
        for(WordListToWord wlw: randomSelectedWlws){
            testObjs.add(makeAnswerList(wlw));
        }
        //??? 이렇게 되면 유저 인풋을 받는 단계가 어려워진다. 컨트롤러에서 만들고 유저인풋까지 받아와서 여기서 처리하도록 다시 만들자.
    }

    private void testWords_code_fragment(List<WordListToWord> randomSelectedWlws) throws Exception {
        StringBuilder OxList = new StringBuilder();

        for (WordListToWord wlw : randomSelectedWlws) {

<<<<<<< Updated upstream
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
=======
//    private void testWords_code_fragment(List<WordListToWord> selectedWlws) throws Exception {
//        StringBuilder OxList = new StringBuilder();
//
//        for (WordListToWord wlw : selectedWlws) {
//            testQuestionObj testObj = makeAnswerList(wlw);
//            int OX = checkRightOrWrong(testObj);
//            OxList.append(OX);
//        }
//        System.out.println(OxList);
//        for(int i = 0; i< selectedWlws.size(); i++){
//            selectedWlws.get(i).updateTestResult(Integer.parseInt(String.valueOf(OxList.charAt(i))));
//        }
//    }
    @Transactional
    public void testWords_code_fragment(ArrayList<testQuestionObj> ObjList) throws Exception {
        StringBuilder OxList = new StringBuilder();

//        ArrayList<WordListToWord> wlws = new ArrayList<>();
//        for(testQuestionObj obj: ObjList){
//            wlws.add(obj.getWlw());
//        }

        for (jpanews.jpaproject1.service.testQuestionObj testQuestionObj : ObjList) {
            int OX = checkRightOrWrong(testQuestionObj);
            OxList.append(OX);
        }

        System.out.println("OX list: "+OxList);
        for(int i = 0; i< ObjList.size(); i++){
            ObjList.get(i).getWlw().updateTestResult(Integer.parseInt(String.valueOf(OxList.charAt(i))));
>>>>>>> Stashed changes
        }
    }
}

package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.MemberRole;
import jpanews.jpaproject1.domain.WordClass;
import jpanews.jpaproject1.domain.WordListToWord;

import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.MemberRepository;
import jpanews.jpaproject1.repository.WordListRepository;
import jpanews.jpaproject1.repository.CustomWordListToWordRepository;
import jpanews.jpaproject1.repository.WordRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class WordListServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;
    @Autowired WordRepository wordRepository;
    @Autowired WordService wordService;
    @Autowired WordListRepository wordListRepository;
    @Autowired WordListService wordListService;
    @Autowired
    CustomWordListToWordRepository wordListToWordRepository;

    @Test
    public void createWordList() throws Exception{

        //==WordList with no words==//
        //given
        //when
        Long savedId = memberService.join("member", "eee", MemberRole.ROLE_USER);
        Long wordList = wordListService.createWordList(savedId);

        //then
        Assertions.assertEquals(wordList, memberService.findOne(savedId).getWordLists().get(1).getId());
        Assertions.assertEquals(0, memberService.findOne(savedId).getWordLists().get(0).getWordListToWords().size());
        Assertions.assertEquals(wordListService.findOneWordList(wordList), memberService.findOne(savedId).getWordLists().get(1));


        //==WordList with words==//
        //will use same member above
        //given
        Word word1 = new Word();
        word1.setName("pool");
        word1.setMeaning("바보");
        word1.setWordClass(WordClass.NOUN);

        Word word2 = new Word();
        word2.setName("cat");
        word2.setMeaning("고양이");
        word2.setWordClass(WordClass.NOUN);

        Word word3 = new Word();
        word3.setName("great");
        word3.setMeaning("aaaaaaaaaa");
        word3.setWordClass(WordClass.ADJECTIVE);


        //when
//        Long savedId2 = memberService.join("member2", "rrr");

        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);


        Long wordList2Id = wordListService.createWordList(savedId, word1, word2, word3);

        //then
        Assertions.assertEquals("member", memberService.findOne(savedId).getUsername());
        Assertions.assertEquals(wordList2Id, memberService.findOne(savedId).getWordLists().get(2).getId());
        Assertions.assertEquals(3,memberService.findOne(savedId).getWordLists().get(2).getWordListToWords().size());
        Assertions.assertEquals(wordListService.findOneWordList(wordList2Id), memberService.findOne(savedId).getWordLists().get(2));
        Assertions.assertEquals(word1, memberService.findOne(savedId).getWordLists().get(2).getWordListToWords().get(0).getWord());
        Assertions.assertEquals(word2, memberService.findOne(savedId).getWordLists().get(2).getWordListToWords().get(1).getWord());
        Assertions.assertEquals(word3, memberService.findOne(savedId).getWordLists().get(2).getWordListToWords().get(2).getWord());
        Assertions.assertEquals(3, memberService.findOne(savedId).getWordLists().size());
        Assertions.assertEquals(3, memberService.findOne(savedId).getWordLists().get(2).getDenominator());
        Assertions.assertEquals(0, memberService.findOne(savedId).getWordLists().get(2).getNumerator());
        Assertions.assertEquals(3, wordListToWordRepository.findAll().size());
    }


    @Test
    public void deleteWordList() throws Exception{
        //given
        Word word1 = new Word();
        word1.setName("pool");
        word1.setMeaning("바보");
        word1.setWordClass(WordClass.NOUN);

        Word word2 = new Word();
        word2.setName("cat");
        word2.setMeaning("고양이");
        word2.setWordClass(WordClass.NOUN);

        Word word3 = new Word();
        word3.setName("great");
        word3.setMeaning("aaaaaaaaaa");
        word3.setWordClass(WordClass.ADJECTIVE);

        //when
        Long savedId = memberService.join("member", "ggg", MemberRole.ROLE_USER);
        Assertions.assertEquals("member", memberService.findOne(savedId).getUsername());

        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);


        Long wordList1 = wordListService.createWordList(memberService.findOne(savedId).getId());
        Long wordList2 = wordListService.createWordList(memberService.findOne(savedId).getId(),word1, word2, word3);
        Long wordList3 = wordListService.createWordList(memberService.findOne(savedId).getId());

        Assertions.assertEquals(4, memberService.findOne(savedId).getWordLists().size());
        Assertions.assertEquals(3, wordListToWordRepository.findAll().size());
        Assertions.assertEquals(3, memberService.findOne(savedId).getWordLists().get(2).getWordListToWords().size());
        Assertions.assertEquals(3,wordListToWordRepository.findAll().size());


        //then
        wordListService.deleteWordList(wordList1);
        Assertions.assertEquals(3, memberService.findOne(savedId).getWordLists().size());
        Assertions.assertEquals(3, wordListService.findAllWordListByMember(memberService.findOne(savedId).getId()).size());

        wordListService.deleteWordList(wordList2);
        Assertions.assertEquals(2, memberService.findOne(savedId).getWordLists().size());
        Assertions.assertEquals(0,wordListToWordRepository.findAll().size());
    }

    @Test
    public void testRandomWords() throws Exception{
        //given
        Word word1 = new Word();
        word1.setName("pool");
        word1.setMeaning("바보");
        word1.setWordClass(WordClass.NOUN);

        Word word2 = new Word();
        word2.setName("cat");
        word2.setMeaning("고양이");
        word2.setWordClass(WordClass.NOUN);

        Word word3 = new Word();
        word3.setName("great");
        word3.setMeaning("very good");
        word3.setWordClass(WordClass.ADJECTIVE);

        Word word4 = new Word();
        word4.setName("catacomb");
        word4.setMeaning("지하묘지");
        word4.setWordClass(WordClass.NOUN);

        Word word5 = new Word();
        word5.setName("meat");
        word5.setMeaning("고기");
        word5.setWordClass(WordClass.NOUN);

        Word word6 = new Word();
        word6.setName("giant");
        word6.setMeaning("big man");
        word6.setWordClass(WordClass.ADJECTIVE);

        Word word7 = new Word();
        word7.setName("wheat");
        word7.setMeaning("plant of rice");
        word7.setWordClass(WordClass.NOUN);

        Word word8 = new Word();
        word8.setName("sky");
        word8.setMeaning("the region of the atmosphere and outer space seen from the earth");
        word8.setWordClass(WordClass.NOUN);

        Word word9 = new Word();
        word9.setName("phone");
        word9.setMeaning("phone");
        word9.setWordClass(WordClass.NOUN);

        Word word10 = new Word();
        word10.setName("java");
        word10.setMeaning("A programming language I am writing now");
        word10.setWordClass(WordClass.NOUN);

        Word word11 = new Word();
        word11.setName("hey");
        word11.setMeaning("어이");
        word11.setWordClass(WordClass.NOUN);

        Word word12 = new Word();
        word12.setName("small");
        word12.setMeaning("of a size that is less than normal or usual.");
        word12.setWordClass(WordClass.ADJECTIVE);

        Word word13 = new Word();
        word13.setName("big");
        word13.setMeaning("of considerable size, extent, or intensity.");
        word13.setWordClass(WordClass.ADJECTIVE);

        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);
        wordService.saveWordToDb(word4);
        wordService.saveWordToDb(word5);
        wordService.saveWordToDb(word6);
        wordService.saveWordToDb(word7);
        wordService.saveWordToDb(word8);
        wordService.saveWordToDb(word9);
        wordService.saveWordToDb(word10);
        wordService.saveWordToDb(word11);
        wordService.saveWordToDb(word12);
        wordService.saveWordToDb(word13);

        //when
        Long memberId = memberService.join("member", "qqq", MemberRole.ROLE_USER);
        Long wordListId = wordListService.createWordList(memberId, word1, word2, word3, word4, word5, word6,word7,word8,word9,word10,word11,word12,word13);

        //==test setting==//
        int howManyTest = 11;
        int howManyWordToTest = 2;
        int howManyWordsExist = 13;
        for(int i=0; i<howManyTest;i++) {
            wordListService.testWords(wordListId, howManyWordToTest);
        }
        //then
        Assertions.assertEquals(2, memberService.findOne(memberId).getWordLists().size());
        Assertions.assertEquals(13, memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().size());



        for(int i=0; i<howManyWordsExist;i++){
            System.out.println("----------------------------------------------------");
            System.out.println("word: " + memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(i).getWord().getName());
            System.out.println("test results: " + memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(i).getRecentTest());
            System.out.println("failed count: " + memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(i).getFailedCount());
            System.out.println("tested count: " + memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(i).getTestedCount());
            System.out.println("correct answer rate: " + memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(i).getCorrectAnswerRate());
            System.out.println("----------------------------------------------------");
        }

        if(howManyWordToTest==13){
            for(int i=0; i<howManyWordToTest;i++){
                Assertions.assertEquals(howManyTest, memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(i).getTestedCount());
            }

            if(howManyTest>=10){
                for(int i=0; i<howManyWordToTest;i++){
                    Assertions.assertEquals(10, memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(i).getRecentTest().length());
                }
            }
        }

        Assertions.assertEquals(0, memberService.findOne(memberId).getWordLists().get(0).getNumerator());
        Assertions.assertEquals(13, memberService.findOne(memberId).getWordLists().get(1).getDenominator());

        memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(1).updateStatus();

        Assertions.assertEquals(1, memberService.findOne(memberId).getWordLists().get(1).getNumerator());
        Assertions.assertEquals(13, memberService.findOne(memberId).getWordLists().get(1).getDenominator());

        memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(2).updateStatus();
        memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(3).updateStatus();
        memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(4).updateStatus();
        memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(5).updateStatus();
        memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(6).updateStatus();

        Assertions.assertEquals(6, memberService.findOne(memberId).getWordLists().get(1).getNumerator());
        Assertions.assertEquals(13, memberService.findOne(memberId).getWordLists().get(1).getDenominator());

        memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(3).updateStatus();
        memberService.findOne(memberId).getWordLists().get(1).getWordListToWords().get(4).updateStatus();

        Assertions.assertEquals(4, memberService.findOne(memberId).getWordLists().get(1).getNumerator());
        Assertions.assertEquals(13, memberService.findOne(memberId).getWordLists().get(1).getDenominator());

        Assertions.assertEquals(1, wordListService.findAllByWordListWithMemorizedStatus(memberService.findOne(memberId).getId(),100).size());
        Assertions.assertEquals(1, wordListService.findAllByWordListWithMemorizedStatus(memberService.findOne(memberId).getId(),95).size());
        Assertions.assertEquals(1, wordListService.findAllByWordListWithMemorizedStatus(memberService.findOne(memberId).getId(),90).size());
        Assertions.assertEquals(1, wordListService.findAllByWordListWithMemorizedStatus(memberService.findOne(memberId).getId(),80).size());
        Assertions.assertEquals(1, wordListService.findAllByWordListWithMemorizedStatus(memberService.findOne(memberId).getId(),60).size());
        Assertions.assertEquals(1, wordListService.findAllByWordListWithMemorizedStatus(memberService.findOne(memberId).getId(),40).size());
        Assertions.assertEquals(1, wordListService.findAllByWordListWithMemorizedStatus(memberService.findOne(memberId).getId(),35).size());
        Assertions.assertEquals(1, wordListService.findAllByWordListWithMemorizedStatus(memberService.findOne(memberId).getId(),31).size());
        Assertions.assertEquals(0, wordListService.findAllByWordListWithMemorizedStatus(memberService.findOne(memberId).getId(),30).size());
        Assertions.assertEquals(0, wordListService.findAllByWordListWithMemorizedStatus(memberService.findOne(memberId).getId(),20).size());
        Assertions.assertEquals(0, wordListService.findAllByWordListWithMemorizedStatus(memberService.findOne(memberId).getId(),0).size());
    }

    @Test
    public void testSelectedWords() throws Exception{
        //given
        Word word1 = new Word();
        word1.setName("pool");
        word1.setMeaning("바보");
        word1.setWordClass(WordClass.NOUN);

        Word word2 = new Word();
        word2.setName("cat");
        word2.setMeaning("고양이");
        word2.setWordClass(WordClass.NOUN);

        Word word3 = new Word();
        word3.setName("great");
        word3.setMeaning("very good");
        word3.setWordClass(WordClass.ADJECTIVE);

        Word word4 = new Word();
        word4.setName("catacomb");
        word4.setMeaning("지하묘지");
        word4.setWordClass(WordClass.NOUN);

        Word word5 = new Word();
        word5.setName("meat");
        word5.setMeaning("고기");
        word5.setWordClass(WordClass.NOUN);

        Word word6 = new Word();
        word6.setName("giant");
        word6.setMeaning("big man");
        word6.setWordClass(WordClass.ADJECTIVE);

        Word word7 = new Word();
        word7.setName("wheat");
        word7.setMeaning("plant of rice");
        word7.setWordClass(WordClass.NOUN);

        Word word8 = new Word();
        word8.setName("sky");
        word8.setMeaning("the region of the atmosphere and outer space seen from the earth");
        word8.setWordClass(WordClass.NOUN);

        Word word9 = new Word();
        word9.setName("phone");
        word9.setMeaning("phone");
        word9.setWordClass(WordClass.NOUN);

        Word word10 = new Word();
        word10.setName("java");
        word10.setMeaning("A programming language I am writing now");
        word10.setWordClass(WordClass.NOUN);

        Word word11 = new Word();
        word11.setName("hey");
        word11.setMeaning("어이");
        word11.setWordClass(WordClass.NOUN);

        Word word12 = new Word();
        word12.setName("small");
        word12.setMeaning("of a size that is less than normal or usual.");
        word12.setWordClass(WordClass.ADJECTIVE);

        Word word13 = new Word();
        word13.setName("big");
        word13.setMeaning("of considerable size, extent, or intensity.");
        word13.setWordClass(WordClass.ADJECTIVE);

        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);
        wordService.saveWordToDb(word4);
        wordService.saveWordToDb(word5);
        wordService.saveWordToDb(word6);
        wordService.saveWordToDb(word7);
        wordService.saveWordToDb(word8);
        wordService.saveWordToDb(word9);
        wordService.saveWordToDb(word10);
        wordService.saveWordToDb(word11);
        wordService.saveWordToDb(word12);
        wordService.saveWordToDb(word13);

        Long memberId = memberService.join("JasonLee", "aass1", MemberRole.ROLE_USER);
        Long wordListId = wordListService.createWordList(memberId, word1, word2, word3, word4, word5, word6, word7, word8, word9, word10, word11, word12, word13);

        //when
        List<WordListToWord> allByWordList = wordListToWordRepository.findAllByWordList(wordListId);

        //then
<<<<<<< Updated upstream
        for(int i=0;i<5;i++){
            wordListService.testWords(wordListId,
                    allByWordList.get(0),
                    allByWordList.get(2),
                    allByWordList.get(4),
                    allByWordList.get(6),
                    allByWordList.get(8));
        }
=======
//        for(int i=0;i<5;i++){
//            wordListService.testWords(
//                    allByWordList.get(0),
//                    allByWordList.get(2),
//                    allByWordList.get(4),
//                    allByWordList.get(6),
//                    allByWordList.get(8));
//        }
>>>>>>> Stashed changes

        for(int i=0; i<9;i+=2){
            Assertions.assertEquals(5, wordListService.findOneWordList(wordListId).getWordListToWords().get(i).getRecentTest().length());
        }

        //test for 'resetTestResults'
        allByWordList.get(4).resetTestResults();
        Assertions.assertEquals(0,allByWordList.get(4).getRecentTest().length());
        Assertions.assertEquals(0,allByWordList.get(4).getTestedCount());
        Assertions.assertEquals(0,allByWordList.get(4).getFailedCount());
        Assertions.assertEquals(0,allByWordList.get(4).getCorrectAnswerRate());

    }


    @Test
    public void addWordsToWordList() throws Exception{

        //==Add==//
        //given
        Word word1 = new Word();
        word1.setName("pool");
        word1.setMeaning("바보");
        word1.setWordClass(WordClass.NOUN);

        Word word2 = new Word();
        word2.setName("cat");
        word2.setMeaning("고양이");
        word2.setWordClass(WordClass.NOUN);

        Word word3 = new Word();
        word3.setName("great");
        word3.setMeaning("very good");
        word3.setWordClass(WordClass.ADJECTIVE);

        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);

        Long memberId = memberService.join("JasonLee", "aass1", MemberRole.ROLE_USER);
        Long wordListId = wordListService.createWordList(memberId, word1, word2);
        Assertions.assertEquals(2, wordListService.findOneWordList(wordListId).getWordListToWords().size());
        //when
        wordListService.addWordsToWordList(wordListId,word3);

        //then
        Assertions.assertEquals(3, wordListService.findOneWordList(wordListId).getWordListToWords().size());

        //==Delete==//
        wordListService.deleteWordsFromWordList(wordListId,wordListService.findOneWordList(wordListId).getWordListToWords().get(1));
        Assertions.assertEquals(2, wordListService.findOneWordList(wordListId).getWordListToWords().size());
        Assertions.assertEquals("pool", wordListToWordRepository.findAllByWordList(wordListId).get(0).getWord().getName());
        Assertions.assertEquals("great", wordListToWordRepository.findAllByWordList(wordListId).get(1).getWord().getName());
        Assertions.assertEquals(2, wordListToWordRepository.findAll().size());
    }

    @Test
    public void memberRoleTest() throws Exception{
        //given
        System.out.println(MemberRole.valueOf("ROLE_USER"));
        System.out.println(MemberRole.valueOf("ROLE_USER").getClass());
        System.out.println(MemberRole.ROLE_USER.getValue());
        System.out.println(MemberRole.ROLE_USER.getValue().getClass());

        Member member = new Member();
        member.setUsername("aasvssd");
        member.setPassword("aAndrks1!");
        member.setRole(MemberRole.ROLE_USER);
        memberService.join(member.getUsername(), member.getPassword(), member.getRole());

        //when


        //then


    }
}
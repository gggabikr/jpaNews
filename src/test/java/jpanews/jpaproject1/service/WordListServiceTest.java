package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.WordClass;
import jpanews.jpaproject1.domain.WordList;
import jpanews.jpaproject1.domain.WordListToWord;
import jpanews.jpaproject1.domain.Words.EngWord;
import jpanews.jpaproject1.domain.Words.KorWord;
import jpanews.jpaproject1.repository.MemberRepository;
import jpanews.jpaproject1.repository.WordListRepository;
import jpanews.jpaproject1.repository.WordListToWordRepository;
import jpanews.jpaproject1.repository.WordRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static jpanews.jpaproject1.domain.WordListToWord.createWordListToWord;
import static org.junit.Assert.*;

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
    @Autowired WordListToWordRepository wordListToWordRepository;

    @Test
    public void createWordList() throws Exception{

        //==WordList with no words==//
        //given
        //when
        Long savedId = memberService.join("member", "eee");
        Long wordList = wordListService.createWordList(savedId);

        //then
        Assertions.assertEquals(wordList, memberService.findOne(savedId).getWordLists().get(0).getId());
        Assertions.assertEquals(0, memberService.findOne(savedId).getWordLists().get(0).getWordListToWords().size());
        Assertions.assertEquals(wordListService.findOneWordList(wordList), memberService.findOne(savedId).getWordLists().get(0));


        //==WordList with words==//
        //will use same member above
        //given
        KorWord word1 = new KorWord();
        word1.setName("pool");
        word1.setKMeaning("바보");
        word1.setWordClass(WordClass.NOUN);

        KorWord word2 = new KorWord();
        word2.setName("cat");
        word2.setKMeaning("고양이");
        word2.setWordClass(WordClass.NOUN);

        EngWord word3 = new EngWord();
        word3.setName("great");
        word3.setEMeaning("aaaaaaaaaa");
        word3.setWordClass(WordClass.ADJECTIVE);


        //when
//        Long savedId2 = memberService.join("member2", "rrr");

        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);


        Long wordList2Id = wordListService.createWordList(savedId, word1, word2, word3);

        //then
        Assertions.assertEquals("member", memberService.findOne(savedId).getUsername());
        Assertions.assertEquals(wordList2Id, memberService.findOne(savedId).getWordLists().get(1).getId());
        Assertions.assertEquals(3,memberService.findOne(savedId).getWordLists().get(1).getWordListToWords().size());
        Assertions.assertEquals(wordListService.findOneWordList(wordList2Id), memberService.findOne(savedId).getWordLists().get(1));
        Assertions.assertEquals(word1, memberService.findOne(savedId).getWordLists().get(1).getWordListToWords().get(0).getWord());
        Assertions.assertEquals(word2, memberService.findOne(savedId).getWordLists().get(1).getWordListToWords().get(1).getWord());
        Assertions.assertEquals(word3, memberService.findOne(savedId).getWordLists().get(1).getWordListToWords().get(2).getWord());
        Assertions.assertEquals(2, memberService.findOne(savedId).getWordLists().size());
        Assertions.assertEquals(3, memberService.findOne(savedId).getWordLists().get(1).getDenominator());
        Assertions.assertEquals(0, memberService.findOne(savedId).getWordLists().get(1).getNumerator());
        Assertions.assertEquals(3, wordListToWordRepository.findAll().size());
    }


    @Test
    public void deleteWordList() throws Exception{
        //given
        KorWord word1 = new KorWord();
        word1.setName("pool");
        word1.setKMeaning("바보");
        word1.setWordClass(WordClass.NOUN);

        KorWord word2 = new KorWord();
        word2.setName("cat");
        word2.setKMeaning("고양이");
        word2.setWordClass(WordClass.NOUN);

        EngWord word3 = new EngWord();
        word3.setName("great");
        word3.setEMeaning("aaaaaaaaaa");
        word3.setWordClass(WordClass.ADJECTIVE);

        //when
        Long savedId = memberService.join("member", "ggg");
        Assertions.assertEquals("member", memberService.findOne(savedId).getUsername());

        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);


        Long wordList1 = wordListService.createWordList(memberService.findOne(savedId).getId());
        Long wordList2 = wordListService.createWordList(memberService.findOne(savedId).getId(),word1, word2, word3);
        Long wordList3 = wordListService.createWordList(memberService.findOne(savedId).getId());

        Assertions.assertEquals(3, memberService.findOne(savedId).getWordLists().size());
        Assertions.assertEquals(3, wordListToWordRepository.findAll().size());
        Assertions.assertEquals(3, memberService.findOne(savedId).getWordLists().get(1).getWordListToWords().size());

        //then
        wordListService.deleteWordList(wordList1);
        Assertions.assertEquals(2, memberService.findOne(savedId).getWordLists().size());
        Assertions.assertEquals(2, wordListService.findAllWordListByMember(memberService.findOne(savedId).getId()).size());

        wordListService.deleteWordList(wordList2);
        Assertions.assertEquals(1, memberService.findOne(savedId).getWordLists().size());
        Assertions.assertEquals(0,wordListToWordRepository.findAll().size());
    }

    @Test
    public void testWords() throws Exception{
        //given
        KorWord word1 = new KorWord();
        word1.setName("pool");
        word1.setKMeaning("바보");
        word1.setWordClass(WordClass.NOUN);

        KorWord word2 = new KorWord();
        word2.setName("cat");
        word2.setKMeaning("고양이");
        word2.setWordClass(WordClass.NOUN);

        EngWord word3 = new EngWord();
        word3.setName("great");
        word3.setEMeaning("very good");
        word3.setWordClass(WordClass.ADJECTIVE);

        KorWord word4 = new KorWord();
        word4.setName("catacomb");
        word4.setKMeaning("지하묘지");
        word4.setWordClass(WordClass.NOUN);

        KorWord word5 = new KorWord();
        word5.setName("meat");
        word5.setKMeaning("고기");
        word5.setWordClass(WordClass.NOUN);

        EngWord word6 = new EngWord();
        word6.setName("giant");
        word6.setEMeaning("big man");
        word6.setWordClass(WordClass.ADJECTIVE);

        EngWord word7 = new EngWord();
        word7.setName("wheat");
        word7.setEMeaning("plant of rice");
        word7.setWordClass(WordClass.NOUN);

        EngWord word8 = new EngWord();
        word8.setName("sky");
        word8.setEMeaning("the region of the atmosphere and outer space seen from the earth");
        word8.setWordClass(WordClass.NOUN);

        EngWord word9 = new EngWord();
        word9.setName("phone");
        word9.setEMeaning("phone");
        word9.setWordClass(WordClass.NOUN);

        EngWord word10 = new EngWord();
        word10.setName("java");
        word10.setEMeaning("A programming language I am writing now");
        word10.setWordClass(WordClass.NOUN);

        KorWord word11 = new KorWord();
        word11.setName("hey");
        word11.setKMeaning("어이");
        word11.setWordClass(WordClass.NOUN);

        EngWord word12 = new EngWord();
        word12.setName("small");
        word12.setEMeaning("of a size that is less than normal or usual.");
        word12.setWordClass(WordClass.ADJECTIVE);

        EngWord word13 = new EngWord();
        word13.setName("big");
        word13.setEMeaning("of considerable size, extent, or intensity.");
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
        Long memberId = memberService.join("member", "qqq");
        Long wordListId = wordListService.createWordList(memberId, word1, word2, word3, word4, word5, word6,word7,word8,word9,word10,word11,word12,word13);

        //==test setting==//
        int howManyTest = 11;
        int howManyWordToTest = 2;
        int howManyWordsExist = 13;
        for(int i=0; i<howManyTest;i++) {
            wordListService.testWords(wordListId, howManyWordToTest);
        }
        //then
        Assertions.assertEquals(1, memberService.findOne(memberId).getWordLists().size());
        Assertions.assertEquals(13, memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().size());



        for(int i=0; i<howManyWordsExist;i++){
            System.out.println("----------------------------------------------------");
            System.out.println("word: " + memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(i).getWord().getName());
            System.out.println("test results: " + memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(i).getRecentTest());
            System.out.println("failed count: " + memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(i).getFailedCount());
            System.out.println("tested count: " + memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(i).getTestedCount());
            System.out.println("correct answer rate: " + memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(i).getCorrectAnswerRate());
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
        Assertions.assertEquals(13, memberService.findOne(memberId).getWordLists().get(0).getDenominator());

        memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(1).updateStatus();

        Assertions.assertEquals(1, memberService.findOne(memberId).getWordLists().get(0).getNumerator());
        Assertions.assertEquals(13, memberService.findOne(memberId).getWordLists().get(0).getDenominator());

        memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(2).updateStatus();
        memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(3).updateStatus();
        memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(4).updateStatus();
        memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(5).updateStatus();
        memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(6).updateStatus();

        Assertions.assertEquals(6, memberService.findOne(memberId).getWordLists().get(0).getNumerator());
        Assertions.assertEquals(13, memberService.findOne(memberId).getWordLists().get(0).getDenominator());

        memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(3).updateStatus();
        memberService.findOne(memberId).getWordLists().get(0).getWordListToWords().get(4).updateStatus();

        Assertions.assertEquals(4, memberService.findOne(memberId).getWordLists().get(0).getNumerator());
        Assertions.assertEquals(13, memberService.findOne(memberId).getWordLists().get(0).getDenominator());

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
}
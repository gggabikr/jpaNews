package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.WordClass;
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

import java.util.ArrayList;
import java.util.List;

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
        Member member = new Member();

        //when
        memberService.join(member);
        Long wordList = wordListService.createWordList(member.getId());

        //then
        Assertions.assertEquals(wordList, member.getWordLists().get(0).getId());
        Assertions.assertEquals(0, member.getWordLists().get(0).getWordListToWords().size());
        Assertions.assertEquals(wordListService.findOneWordList(wordList), member.getWordLists().get(0));


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
        memberService.join(member);

        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);

        List<Long> wordIdList = new ArrayList<>();
        wordIdList.add(word1.getId());
        wordIdList.add(word2.getId());
        wordIdList.add(word3.getId());

        Long wordList2 = wordListService.createWordList(member.getId(), wordIdList);

        //then
        Assertions.assertEquals(member, memberService.findOne(member.getId()));
        Assertions.assertEquals(wordList2, member.getWordLists().get(1).getId());
        Assertions.assertEquals(3,member.getWordLists().get(1).getWordListToWords().size());
        Assertions.assertEquals(wordListService.findOneWordList(wordList2), member.getWordLists().get(1));
        Assertions.assertEquals(word1, member.getWordLists().get(1).getWordListToWords().get(0).getWord());
        Assertions.assertEquals(word2, member.getWordLists().get(1).getWordListToWords().get(1).getWord());
        Assertions.assertEquals(word3, member.getWordLists().get(1).getWordListToWords().get(2).getWord());
        Assertions.assertEquals(2, member.getWordLists().size());
        Assertions.assertEquals(3, member.getWordLists().get(1).getDenominator());
        Assertions.assertEquals(0, member.getWordLists().get(1).getNumerator());
        Assertions.assertEquals(3, wordListToWordRepository.findAll().size());
    }


    @Test
    public void deleteWordList() throws Exception{
        //given
        Member member = new Member();

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
        memberService.join(member);
        Assertions.assertEquals(member, memberService.findOne(member.getId()));

        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);

        List<Long> wordIdList = new ArrayList<>();
        wordIdList.add(word1.getId());
        wordIdList.add(word2.getId());
        wordIdList.add(word3.getId());

        Long wordList1 = wordListService.createWordList(member.getId());
        Long wordList2 = wordListService.createWordList(member.getId(),wordIdList);
        Long wordList3 = wordListService.createWordList(member.getId());

        Assertions.assertEquals(3, member.getWordLists().size());
        Assertions.assertEquals(3, wordListToWordRepository.findAll().size());
        Assertions.assertEquals(3, member.getWordLists().get(1).getWordListToWords().size());
        //1번 지우고 멤버의 단어장 수 체크 - 지우기 기능이 작동안함. @@이유를 찾아보자@@
        //2번 지우고 동일작업+ wlw 지워졌는지?? 지워지는게 맞나?
        //안지워진다면 wlw의 wordlist가 null인지 아닌지 확인하거나...암튼.

        //then
//        System.out.println("member's wordList: "+ member.getWordLists());
        System.out.println("member's wordList before line below"+wordListService.findAllWordListByMember(member.getId()));
        wordListService.deleteWordList(wordList1);
        System.out.println("member's wordList after line above"+wordListService.findAllWordListByMember(member.getId()));

        System.out.println("member's wordList: "+ member.getWordLists());
        Assertions.assertEquals(2, member.getWordLists().size());
        Assertions.assertEquals(2, wordListService.findAllWordListByMember(member.getId()).size());

        wordListService.deleteWordList(wordList2);

        Assertions.assertEquals(2,wordListToWordRepository.findAll().size());
    }

    @Test
    public void makeAnswerList() throws Exception{
        //given


        //when


        //then


    }

    @Test
    public void checkRightOrWrong() throws Exception{
        //given


        //when


        //then


    }

    @Test
    public void testWords() throws Exception{
        //given


        //when


        //then


    }
}
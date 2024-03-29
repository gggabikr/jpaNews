package jpanews.jpaproject1.service;


import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.MemberRole;
import jpanews.jpaproject1.domain.WordClass;

import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.MemberRepository;
import jpanews.jpaproject1.repository.CustomWordListToWordRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired WordListService wordListService;
    @Autowired
    CustomWordListToWordRepository wordListToWordRepository;
    @Autowired WordService wordService;

    @Test
    public void JoinMember() throws Exception{
        //given
        //when
        Long savedId = memberService.join("JasonLee","aaa", MemberRole.ROLE_USER);

        //then
        assertEquals(savedId, memberRepository.findByUsername("JasonLee").get(0).getId());
        assertEquals(savedId, memberRepository.findOne(savedId).getId());

    }

    @Test(expected = IllegalStateException.class)
    public void exceptionForDuplicated() throws Exception{

        //given
        //when
        //for the below code, error should not be arisen -> fail() method need to be worked
        //memberService.join("Breece2","bbb");

        memberService.join("Breece","bbb", MemberRole.ROLE_USER);
        memberService.join("Breece","ccc", MemberRole.ROLE_USER); //error should be arisen

        //then
        fail("error must be arisen");
    }

    @Test
    public void login() throws Exception{
        //given
        Long memberId1 = memberService.join("JasonLee", "abcdef", MemberRole.ROLE_USER);
        Long memberId2 = memberService.join("Jasonlee", "abcdef", MemberRole.ROLE_USER);


        //when
        Boolean loginSuccess = memberService.login("JasonLee", "abcdef");
        Boolean loginFail1 = memberService.login("JasonLee", "abcdee");
        Boolean loginFail2 = memberService.login("JasonLee", "abcdeF");
        Boolean loginFail3 = memberService.login("JasonLee", "abcDef");
        Boolean loginFail4 = memberService.login("JasonLee", "ABCDEF");
        Boolean loginFail5 = memberService.login("JasonLee", "abcde!");
        Boolean loginFail6 = memberService.login("JasonLee", "abcde.");
        Boolean loginFail7 = memberService.login("JasonLee", "ab_def");
        Boolean loginFail8 = memberService.login("JasonLee", "abcdefg");
        Boolean loginFail9 = memberService.login("JasonLee", "ab#cdef");
        Boolean loginFail10 = memberService.login("JasonLee", "abcd@f");

        Boolean loginSuccess2 = memberService.login("Jasonlee", "abcdef");
        Boolean loginFail11 = memberService.login("Jasonlee", "abcdee");
        Boolean loginFail12 = memberService.login("Jasonlee", "abcdeF");
        Boolean loginFail13 = memberService.login("Jasonlee", "abcDef");
        Boolean loginFail14 = memberService.login("Jasonlee", "ABCDEF");
        Boolean loginFail15 = memberService.login("Jasonlee", "abcde!");
        Boolean loginFail16 = memberService.login("Jasonlee", "abcde.");
        Boolean loginFail17 = memberService.login("Jasonlee", "ab_def");
        Boolean loginFail18 = memberService.login("Jasonlee", "abcdefg");
        Boolean loginFail19 = memberService.login("Jasonlee", "ab#cdef");
        Boolean loginFail20 = memberService.login("Jasonlee", "abcd@f");


        //then
        Assertions.assertEquals(true, loginSuccess);
        Assertions.assertEquals(false, loginFail1);
        Assertions.assertEquals(false, loginFail2);
        Assertions.assertEquals(false, loginFail3);
        Assertions.assertEquals(false, loginFail4);
        Assertions.assertEquals(false, loginFail5);
        Assertions.assertEquals(false, loginFail6);
        Assertions.assertEquals(false, loginFail7);
        Assertions.assertEquals(false, loginFail8);
        Assertions.assertEquals(false, loginFail9);
        Assertions.assertEquals(false, loginFail10);

        Assertions.assertEquals(true, loginSuccess);
        Assertions.assertEquals(false, loginFail11);
        Assertions.assertEquals(false, loginFail12);
        Assertions.assertEquals(false, loginFail13);
        Assertions.assertEquals(false, loginFail14);
        Assertions.assertEquals(false, loginFail15);
        Assertions.assertEquals(false, loginFail16);
        Assertions.assertEquals(false, loginFail17);
        Assertions.assertEquals(false, loginFail18);
        Assertions.assertEquals(false, loginFail19);
        Assertions.assertEquals(false, loginFail20);
    }

    @Test
    public void changePassword() throws Exception{
        //given
        Long memberId = memberService.join("JasonLee", "abcdef", MemberRole.ROLE_USER);
        Assertions.assertEquals(true, memberService.login("JasonLee", "abcdef"));
        Long wordListId = wordListService.createWordList(memberId);

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

        Long wordListId2 = wordListService.createWordList(memberId, word1, word2, word3);
        wordListService.findOneWordList(wordListId).saveWordListToWord();


        //when
        Long memberIdAfterChangePassword = memberService.changePassword(memberId, "abcccd");

        //then
        Assertions.assertEquals(false, memberService.login("JasonLee", "abcdef"));
        Assertions.assertEquals(true, memberService.login("JasonLee", "abcccd"));
        Assertions.assertEquals(memberId, memberIdAfterChangePassword);
        Assertions.assertEquals(wordListId2, memberService.findOne(memberId).getWordLists().get(1).getId());
        Assertions.assertEquals(wordListToWordRepository.findAllByWordList(wordListId2), memberService.findByUsername("JasonLee").get(0).getWordLists().get(1).getWordListToWords());

    }

    @Test
    public void findAllMembers() throws Exception{
        //given
        Long member1 = memberService.join("JasonLee", "aabbccd", MemberRole.ROLE_USER);
        Long member2 = memberService.join("BreecePark", "ssddff", MemberRole.ROLE_USER);
        Long member3 = memberService.join("DanielLee", "ffgghh", MemberRole.ROLE_USER);

        //when
        List<Member> members = memberService.findAllMembers();

        //then
        Assertions.assertEquals("JasonLee", members.get(0).getUsername());
        Assertions.assertEquals("BreecePark", members.get(1).getUsername());
        Assertions.assertEquals("DanielLee", members.get(2).getUsername());
    }

//    @Test
//    public void enumTest() throws Exception{
//        System.out.println(WordClass.ADVERB);
//        System.out.println(WordClass.ADVERB.toString());
//
//        System.out.println(WordClass.ADVERB.getClass());
//        System.out.println(WordClass.ADVERB.toString().getClass());
//
//    }
}
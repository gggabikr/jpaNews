package jpanews.jpaproject1.service;


import jpanews.jpaproject1.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    public void JoinMember() throws Exception{
        //given
        //when
        Long savedId = memberService.join("JasonLee","aaa");

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

        memberService.join("Breece","bbb");
        memberService.join("Breece","ccc"); //error should be arisen

        //then
        fail("error must be arisen");
    }

    @Test
    public void login() throws Exception{
        //given
        Long memberId1 = memberService.join("JasonLee", "abcdef");
        Long memberId2 = memberService.join("Jasonlee", "abcdef");


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
package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.WordClass;
import jpanews.jpaproject1.repository.MemberRepository;
import org.junit.Test;
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
    public void enumTest() throws Exception{
        System.out.println(WordClass.ADVERB);
        System.out.println(WordClass.ADVERB.toString());

        System.out.println(WordClass.ADVERB.getClass());
        System.out.println(WordClass.ADVERB.toString().getClass());

    }
}
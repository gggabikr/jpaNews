package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.WordClass;
import jpanews.jpaproject1.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void JoinMember() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("JasonLee");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findByUsername("JasonLee").get(0));
        assertEquals(member, memberRepository.findOne(savedId));
        assertEquals(member.getPassword(), memberRepository.findOne(savedId).getPassword());

    }

    @Test(expected = IllegalStateException.class)
    public void exceptionForDuplicated() throws Exception{
        //given
        Member member1 = new Member();
        Member member2 = new Member();

        member1.setUsername("Breece");
        member2.setUsername("Breece");

//        for the below code, error should not be arisen -> fail() method need to be worked
//        member2.setUsername("Breece2");

        //when
        memberService.join(member1);
        memberService.join(member2); //error should be arisen

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
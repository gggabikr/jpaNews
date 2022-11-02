package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;


    @Test
    @Transactional
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("memberA");
        //when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.findOne(savedId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername())
                .isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);

    }

//    아래 테스트는 memberServiceTest 단계에서 실행되어야 한다.
//    왜냐하면 duplicated name을 체크하는 메서드가 서비스에있기때문.
//    하지만 그냥 같은 에러체킹부분을 memberRepository 에도 만듦으로써 여기서도 테스트 가능하도록 만들었다.
    @Test(expected = IllegalStateException.class)
    @Transactional
    public void validationForDuplicatedUsername() throws Exception{
        //given
        Member member1 = new Member();
        member1.setUsername("memberA");
        Member member2 = new Member();
        member2.setUsername("memberA");

        //when

        memberRepository.save(member1);
        em.flush();
        memberRepository.save(member2); //error need to be raised here

        //then
        Assertions.fail("error need to be raised.");

    }


}
package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //Sign Up
    @Transactional
    public Long join(String userName, String password) throws Exception{
        Member member = new Member();
        member.setUsername(userName);
        //checking for a username if it is duplicated.
        validateDuplicateMember(member);

        //encoding the password
        String enPw = passwordEncoder.encode(password);
        member.setPassword(enPw);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByUsername(member.getUsername());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("There is an user with a same username.");
        }
    }

    //referring all the members
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //referring one member
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}

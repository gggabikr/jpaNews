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

    //이거 비번 바꾼후에 save할때 업데이트 제대로 되는지
    // (save할때 아이디가 이미 있는 아이디라면~~ 아니면~~ 하는 방법 JPAShop에 있었던걸로 기억)
    //그리고 테스트 하기. (비번이 바뀌었는지,새비번으로 로그인 되는지, 이전비번으로 로그인 실패하는지,
    // 가입후 워드리스트, wlw등 생성하고, 그 후 비번 바꿈. 기존것들 다 연동되어있는지 체크등등.)
    @Transactional
    public Long changePassword(Long memberId, String newPassword) throws Exception{
        Member member = memberRepository.findOne(memberId);
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        return memberId;
    }

    public Boolean login(String username, String rawPw){
        Member member = memberRepository.findByUsername(username).get(0);
        return passwordEncoder.matches(rawPw, member.getPassword());
    }

    //referring all the members
    public List<Member> findAllMembers(){
        return memberRepository.findAll();
    }

    //referring one member
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}

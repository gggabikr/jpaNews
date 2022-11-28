package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.MemberRole;
import jpanews.jpaproject1.repository.MemberRepository;
import jpanews.jpaproject1.repository.WordListRepository;
import jpanews.jpaproject1.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final WordListService wordListService;
    private final PasswordEncoder passwordEncoder;

    //Sign Up
    @Transactional
    public Long join(String userName, String password, MemberRole role) throws Exception {
        Member member = new Member();
        member.setUsername(userName);
        //checking for a username if it is duplicated.
        validateDuplicateMember(member);

        member.setRole(role);

        //encoding the password
        String enPw = passwordEncoder.encode(password);
        member.setPassword(enPw);
        Long memberId = memberRepository.save(member);

        //create default wordList when a member account is created
        wordListService.createWordList(memberId);
        return memberId;
//        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByUsername(member.getUsername());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("There is an user with a same username.");
        }
    }

    //비번 최소 길이, 최대길이, 소문자 대문자 등등 체크하는 기능 넣기

    @Transactional
    public Long changePassword(Long memberId, String newPassword) throws Exception {
        Member member = memberRepository.findOne(memberId);
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        return memberId;
    }

    public Boolean login(String username, String rawPw) {
        Member member = memberRepository.findByUsername(username).get(0);
        return passwordEncoder.matches(rawPw, member.getPassword());
    }

    //referring all the members
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    //referring one member
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public List<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Member> member = memberRepository.findByUsername(username);
        if(member.size() == 0) {
            throw new UsernameNotFoundException("Cannot find the username: " + username);
        }
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        if (member.get(0).getRole() == MemberRole.ROLE_ADMIN){
//        if ("admin".equals(username)) {
//            authorities.add(new SimpleGrantedAuthority(MemberRole.ROLE_ADMIN.getValue()));
//        } else {
//            authorities.add(new SimpleGrantedAuthority(MemberRole.ROLE_USER.getValue()));
//        }
        System.out.println("Member found for: " + username + ". Will try login");
            return new PrincipalDetails(member.get(0));
    }


}

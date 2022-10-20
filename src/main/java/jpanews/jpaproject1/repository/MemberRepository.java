package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Long save(Member member) throws Exception{
        if(!this.findByUsername(member.getUsername()).isEmpty()){
            if(!Objects.equals(member.getId(), this.findByUsername(member.getUsername()).get(0).getId())){
                throw new IllegalStateException("There is an user with a same username.");
            } else {
                em.merge(member);
            }
        }
        em.persist(member);
        return member.getId();
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    public List<Member> findByUsername(String username){
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }
}
//@Query("SELECT w.name FROM Word w WHERE UPPER(w.name) LIKE CONCAT('%',UPPER(TRIM((:part)),'%'))
//List<String> findWordsWithPart(@Param("part") String part);
//
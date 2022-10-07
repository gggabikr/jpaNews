package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.WordList;
import jpanews.jpaproject1.domain.Words.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WordListRepository {

    private final EntityManager em;

    public void save(WordList wordList){
        em.persist(wordList);
    }

    public WordList findOne(Long id){
        return em.find(WordList.class, id);
    }

    public List<WordList> findAllByMember(Long memberId){
        return em.createQuery(
                        "SELECT wl FROM WordList wl WHERE wl.member = :memberId", WordList.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

//    //혹은 위 메서드에 Lang 변수 넣는식으로 하고 하나 없애기
//    public List<WordList> findAllEngByMember(Long memberId){
//    }
//    public List<WordList> findAllKorByMember(Long memberId) {
//    }

    public List<WordList> findAllByMemorizedStatus(Long memberId, int percent){
        return em.createQuery("SELECT wl FROM WordList wl WHERE wl.numerator/wl.denominator < :percent AND wl.member = :memberId",WordList.class)
                .setParameter("percent",percent).setParameter("memberId", memberId)
                .getResultList();
    }
}

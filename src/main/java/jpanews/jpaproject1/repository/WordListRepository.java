package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.WordList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
}

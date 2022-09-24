package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.Words.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WordRepository {

    private final EntityManager em;

    public Long save(Word word){
        if(word.getId() == null){
            em.persist(word);
        } else{
            //temporarily made it merged. will be changed later.
            em.merge(word);
        }
        return word.getId();
    }

    public Word findOne(Long id){
        return em.find(Word.class, id);
    }

    public List<Word> findAll(){
        return em.createQuery("select w from Word w",Word.class).getResultList();
    }

    public List<Word> findAllKorWord(){
        return em.createQuery("select w from Word w WHERE w.language = 'K'",Word.class).getResultList();
    }

    public List<Word> findAllEngWord(){
        return em.createQuery("select w from Word w WHERE w.language = 'E'",Word.class).getResultList();
    }
//    public List<Word> findByName(@Param("name") String name){
//        return em.createQuery(
//                "SELECT w FROM Word w WHERE UPPER(w.name) LIKE UPPER(TRIM(:name))",Word.class)
//                .getResultList();
//    }


    public List<Word> findByName(String name) throws Exception{
        occurTooShortInputException(name);
        return em.createQuery(
                        "SELECT w FROM Word w WHERE UPPER(w.name) LIKE UPPER(TRIM(:name))",Word.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Word> findByNameStartingWith(String name) throws Exception{
        occurTooShortInputException(name);
        return em.createQuery(
                        "SELECT w FROM Word w WHERE UPPER(w.name) LIKE CONCAT(UPPER(TRIM(:name)), '%')",Word.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Word> findByNameEndingWith(String name) throws Exception{
        occurTooShortInputException(name);
        return em.createQuery(
                        "SELECT w FROM Word w WHERE UPPER(w.name) LIKE CONCAT('%' ,UPPER(TRIM(:name)))",Word.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Word> findByNameContaining(String name) throws Exception{
        occurTooShortInputException(name);
        return em.createQuery(
                        "SELECT w FROM Word w WHERE UPPER(w.name) LIKE CONCAT('%' ,UPPER(TRIM(:name)), '%')",Word.class)
                .setParameter("name", name)
                .getResultList();
    }


    public void occurTooShortInputException(String input){
        if (input.length() < 3) {
            throw new IllegalStateException("The length of input must be more than 2.");
        }
    }
}

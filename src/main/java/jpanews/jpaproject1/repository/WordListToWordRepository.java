package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.WordList;
import jpanews.jpaproject1.domain.WordListToWord;
import jpanews.jpaproject1.domain.Words.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WordListToWordRepository {

    private final EntityManager em;

//    public void addWLW(WordListToWord wlw){
//        em.persist(wlw);
//    }

    public List<WordListToWord> findAll(){
        return em.createQuery(
                        "SELECT wlw FROM WordListToWord wlw", WordListToWord.class)
                .getResultList();
    }



    public List<WordListToWord> RandomSelect(Long wordListId, int number) {
        List<WordListToWord> selectedWordList =
                em.createQuery("SELECT wlw FROM WordListToWord wlw WHERE wlw.id = :wordListId", WordListToWord.class)
                .setParameter("wordListId", wordListId)
                .getResultList();
        int qty = selectedWordList.size();

        if(number>qty){
            number = qty;
        }

        ArrayList<Integer> selectedNumbers = new ArrayList<>();

        while(selectedNumbers.size()<number){
            int randomNumber = (int)(Math.random() * qty);
            if(!selectedNumbers.contains(randomNumber)){
                selectedNumbers.add(randomNumber);
            }
        }
        ArrayList<WordListToWord> selectedWordListToWords = new ArrayList<>();
        for (int index:selectedNumbers) {
            selectedWordListToWords.add(selectedWordList.get(index));
        }

        return selectedWordListToWords;
    }
}

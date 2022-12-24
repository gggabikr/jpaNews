package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.WordListToWord;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CustomWordListToWordRepositoryImpl implements CustomWordListToWordRepository{

    private final EntityManager em;

    @Override
    public List<WordListToWord> findAll(){
        return em.createQuery(
                        "SELECT wlw FROM WordListToWord wlw", WordListToWord.class)
                .getResultList();
    }

    @Override
    public List<WordListToWord> findAllByWordList(Long wordListId){
        return em.createQuery(
                        "SELECT wlw FROM WordListToWord wlw WHERE wlw.wordList.id=:wordListId",WordListToWord.class)
                .setParameter("wordListId", wordListId)
                .getResultList();
    }

    @Override
    public List<WordListToWord> RandomSelect(Long wordListId, int number) {
        List<WordListToWord> selectedWordList =
                em.createQuery("SELECT wlw FROM WordListToWord wlw WHERE wlw.wordList.id = :wordListId", WordListToWord.class)
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

    @Override
    public void deleteWlw(Long wlwId){
        em.remove(em.find(WordListToWord.class, wlwId));
    }

    @Override
    public void createWlw(WordListToWord wlw) {
        em.persist(wlw);
        em.flush();
    }
}

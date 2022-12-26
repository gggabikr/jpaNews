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
    public WordListToWord findOne(Long wlwId) {
        return em.find(WordListToWord.class, wlwId);
    }


    @Override
    public List<WordListToWord> findByWordIdAndWordListId(Long wordListId, Long wordId) {
        return em.createQuery("SELECT wlw FROM WordListToWord wlw WHERE wlw.wordList.id = :wordListId AND wlw.word.id = :wordId", WordListToWord.class)
                .setParameter("wordListId", wordListId)
                .setParameter("wordId", wordId)
                .getResultList();
    }

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
    public void deleteWlw(Long wordListId, Long wordId) {
        List<WordListToWord> WLWsByWordIdAndWordListId = findByWordIdAndWordListId(wordListId, wordId);
        System.out.println("WLWs list: " + WLWsByWordIdAndWordListId);
        if (WLWsByWordIdAndWordListId.size() == 1) {
            em.remove(WLWsByWordIdAndWordListId.get(0));
        } else{
            throw new IllegalArgumentException("More than one WLW is found for given WordId and WordListId.");
        }
    }

    @Override
    public void createWlw(WordListToWord wlw) {
        Long wordId = wlw.getWord().getId();
        Long wordListId = wlw.getWordList().getId();
        if(findByWordIdAndWordListId(wordId, wordListId).size() ==0){
            em.persist(wlw);
            em.flush();
        } else{
            System.out.println(
                    "The WLW with wordID: "+ wordId +
                            " & wordListId: " + wordListId + " is already exist. " +
                            "It will not added to your list.");
        }

    }
}

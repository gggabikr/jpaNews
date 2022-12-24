package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.WordListToWord;
import java.util.List;

public interface CustomWordListToWordRepository {

    public List<WordListToWord> findAll();

    public List<WordListToWord> findAllByWordList(Long wordListId);

    public List<WordListToWord> RandomSelect(Long wordListId, int number);

    public void deleteWlw(Long wlwId);

    public void createWlw(WordListToWord wlw);
}

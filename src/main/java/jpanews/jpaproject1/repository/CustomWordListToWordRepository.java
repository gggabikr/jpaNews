package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.WordListToWord;
import java.util.List;

public interface CustomWordListToWordRepository {

    public WordListToWord findOne(Long wlwId);

    public List<WordListToWord> findByWordIdAndWordListId(Long wordId, Long wordListId);

    public List<WordListToWord> findAllByWordListWithWordData(Long wordListId);

    public List<WordListToWord> findAll();

    public List<WordListToWord> findAllByWordList(Long wordListId);

    public List<WordListToWord> RandomSelect(Long wordListId, int number);

    public void deleteWlw(Long wlwId);

    public void deleteWlw(Long wordListId, Long wordId);

    public void createWlw(WordListToWord wlw);
}

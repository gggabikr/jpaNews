package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.WordList;
import jpanews.jpaproject1.domain.WordListToWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordListToWordRepository extends JpaRepository<WordListToWord, Long>, CustomWordListToWordRepository {
    Page<WordListToWord> findAllByWordListIdOrderByAddDate(Long wordListId, Pageable pageable);

    List<WordListToWord> findAllByWordListIdOrderByIdDesc(Long wordListId, Pageable pageable);
}
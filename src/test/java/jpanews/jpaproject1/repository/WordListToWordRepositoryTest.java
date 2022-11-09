package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.MemberRole;
import jpanews.jpaproject1.domain.WordClass;
import jpanews.jpaproject1.domain.WordListToWord;
import jpanews.jpaproject1.domain.Words.EngWord;
import jpanews.jpaproject1.domain.Words.KorWord;
import jpanews.jpaproject1.service.MemberService;
import jpanews.jpaproject1.service.WordListService;
import jpanews.jpaproject1.service.WordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;


@RunWith(SpringRunner.class)
@SpringBootTest
public class WordListToWordRepositoryTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired WordListRepository wordListRepository;
    @Autowired WordListService wordListService;
    @Autowired WordListToWordRepository wordListToWordRepository;
    @Autowired WordRepository wordRepository;
    @Autowired WordService wordService;
    @Autowired EntityManager em;

    @Test
    public void customMethods() throws Exception{
        //given
        KorWord word1 = new KorWord();
        word1.setName("pool");
        word1.setKMeaning("바보");
        word1.setWordClass(WordClass.NOUN);

        KorWord word2 = new KorWord();
        word2.setName("cat");
        word2.setKMeaning("고양이");
        word2.setWordClass(WordClass.NOUN);

        EngWord word3 = new EngWord();
        word3.setName("great");
        word3.setEMeaning("very good");
        word3.setWordClass(WordClass.ADJECTIVE);

        KorWord word4 = new KorWord();
        word4.setName("catacomb");
        word4.setKMeaning("지하묘지");
        word4.setWordClass(WordClass.NOUN);

        KorWord word5 = new KorWord();
        word5.setName("meat");
        word5.setKMeaning("고기");
        word5.setWordClass(WordClass.NOUN);

        EngWord word6 = new EngWord();
        word6.setName("giant");
        word6.setEMeaning("big man");
        word6.setWordClass(WordClass.ADJECTIVE);

        Long memberId = memberService.join("JasonLee", "aaaaaa", MemberRole.ROLE_USER);

        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);
        wordService.saveWordToDb(word4);
        wordService.saveWordToDb(word5);
        wordService.saveWordToDb(word6);

        Long wordListId = wordListService.createWordList(memberId, word1, word2, word3,word4,word5,word6);

        //when
//        wordListToWordRepository.RandomSelect(wordListId, 2);
        Pageable pageable = PageRequest.of(1,2, Sort.by("id"));
        System.out.println("page: " + wordListToWordRepository.findAllByWordListIdOrderByIdDesc(wordListId, pageable));
        Pageable pageable2 = PageRequest.of(2,2, Sort.by("id"));
        System.out.println("page: " + wordListToWordRepository.findAllByWordListIdOrderByIdDesc(wordListId, pageable2));
        Pageable pageable3 = PageRequest.of(1,3, Sort.by("id"));
        System.out.println("page: " + wordListToWordRepository.findAllByWordListIdOrderByIdDesc(wordListId, pageable3));

        //then
//        List<WordListToWord> all = wordListToWordRepository.findAll();

//        System.out.println(all.get(0).getWord().getId());
//        System.out.println(wordService.findById(2L).getName());

        for (WordListToWord wlw: wordListToWordRepository.findAllByWordListIdOrderByIdDesc(wordListId, pageable3)) {
            Long id = wlw.getWord().getId();
            System.out.println(id);
            System.out.println(wordService.findById(id).getName());
        }

    }
}
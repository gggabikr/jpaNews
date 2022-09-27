package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.WordClass;
import jpanews.jpaproject1.domain.Words.EngWord;
import jpanews.jpaproject1.domain.Words.KorWord;
import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.WordRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class WordServiceTest {

    @Autowired WordRepository wordRepository;
    @Autowired WordService wordService;

    @Test
    public void saveWordToDb() throws Exception {
        //given
        KorWord word1 = new KorWord();
        word1.setName("pool");
        word1.setKMeaning("바보");
        word1.setWordClass(WordClass.NOUN);

        KorWord word2 = new KorWord();
        word2.setName("cat");
        word2.setKMeaning("고양이");
        word2.setWordClass(WordClass.NOUN);

        //when
        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);

        //then
        Assertions.assertEquals(word1, wordService.findById(word1.getId()));
        Assertions.assertEquals(1, wordService.findWithString("pool").size());
        Assertions.assertEquals(word1.getName(), wordService.findWithString("pool").get(0).getName());
        Assertions.assertEquals("NOUN", wordService.findWithString("pool").get(0).getWordClass().toString());

        List<Word> list = new ArrayList<>();
        list.add(word1);
        list.add(word2);

        Assertions.assertEquals(list, wordService.findAllKorWords());
        Assertions.assertEquals(list, wordService.findAllWords());
        Assertions.assertEquals(list, wordService.findWithWordClass("NOUN"));
        Assertions.assertEquals(list, wordService.findWithWordClass("Noun"));
        Assertions.assertEquals(list, wordService.findWithWordClass("nOUn"));
        Assertions.assertEquals(list, wordService.findWithWordClass("noun"));

    }

    @Test
    public void findById() throws Exception {
        //given
        KorWord word1 = new KorWord();
        word1.setName("pool");
        word1.setKMeaning("바보");
        word1.setWordClass(WordClass.NOUN);

        KorWord word2 = new KorWord();
        word2.setName("cat");
        word2.setKMeaning("고양이");
        word2.setWordClass(WordClass.NOUN);

        //when
        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);

        //then
        Assertions.assertEquals(word1, wordService.findById(word1.getId()));
        Assertions.assertEquals(word2, wordService.findById(word2.getId()));
    }

    @Test
    public void findAllWords() throws Exception {
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
        word3.setEMeaning("aaaaaaaaaa");
        word3.setWordClass(WordClass.ADJECTIVE);
        //when
        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);

        //then

        //test for findAllWords
        Assertions.assertEquals(word1, wordService.findAllWords().get(0));
        Assertions.assertEquals(word2, wordService.findAllWords().get(1));
        Assertions.assertEquals(word3, wordService.findAllWords().get(2));

        Assertions.assertEquals(3, wordService.findAllWords().size());
        Assertions.assertEquals(word2.getWordClass(), wordService.findAllWords().get(1).getWordClass());
        Assertions.assertEquals(word3.getWordClass(), wordService.findAllWords().get(2).getWordClass());

        //test for findAllKorWords
        Assertions.assertEquals(word1, wordService.findAllKorWords().get(0));
        Assertions.assertEquals(word2, wordService.findAllKorWords().get(1));
        Assertions.assertEquals(2, wordService.findAllKorWords().size());
        Assertions.assertEquals(word2.getWordClass(), wordService.findAllKorWords().get(1).getWordClass());

        //test for findAllEngWords
        Assertions.assertEquals(word3, wordService.findAllEngWords().get(0));
        Assertions.assertEquals(1, wordService.findAllEngWords().size());
        Assertions.assertEquals(word3.getWordClass(), wordService.findAllEngWords().get(0).getWordClass());
        Assertions.assertEquals(word3.getMeaning(), wordService.findAllEngWords().get(0).getMeaning());
        Assertions.assertEquals(word3.getEMeaning(), wordService.findAllEngWords().get(0).getMeaning());

        //test for findWithWordClass
        Assertions.assertEquals(2, wordService.findWithWordClass("Noun").size());
        Assertions.assertEquals(2, wordService.findWithWordClass("NoUn").size());
        Assertions.assertEquals(word1, wordService.findWithWordClass("NoUn").get(0));
        Assertions.assertEquals(word2, wordService.findWithWordClass("nOun").get(1));

        Assertions.assertEquals(1, wordService.findWithWordClass("Adjective").size());
        Assertions.assertEquals(word3.getMeaning(), wordService.findWithWordClass("adjecTIve").get(0).getMeaning());

    }



    @Test
    public void findWithString() {
        //given


        //when


        //then

    }

    @Test
    public void findWithWordClass() throws Exception{
        //given


        //when


        //then


    }
}
package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.WordClass;
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
        Assertions.assertEquals(word1, wordRepository.findOne(word1.getId()));
        Assertions.assertEquals(1, wordRepository.findByName("pool").size());
        Assertions.assertEquals(word1.getName(), wordRepository.findByName("pool").get(0).getName());
        Assertions.assertEquals("NOUN", wordRepository.findAllKorWord().get(0).getWordClass().toString());

        List<Word> list = new ArrayList<>();
        list.add(word1);
        list.add(word2);
        Assertions.assertEquals(list, wordRepository.findAllKorWord());
        Assertions.assertEquals(list, wordRepository.findAll());
        Assertions.assertEquals(list, wordRepository.findByWordClass("NOUN"));
        Assertions.assertEquals(list, wordRepository.findByWordClass("Noun"));
        Assertions.assertEquals(list, wordRepository.findByWordClass("nOUn"));
        Assertions.assertEquals(list, wordRepository.findByWordClass("noun"));

    }

    @Test
    public void findById() {
        //given


        //when


        //then

    }

    @Test
    public void findAllWords() {
        //given


        //when


        //then

    }

    @Test
    public void findAllKorWords() {
        //given


        //when


        //then

    }

    @Test
    public void findAllEngWords() {
        //given


        //when


        //then

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
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

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class WordServiceTest {

    @Autowired WordRepository wordRepository;
    @Autowired WordService wordService;

    @Test
    public void saveWordToDb() throws Exception {
        KorWord word = new KorWord();
        word.setKMeaning("바보");
        word.setWordClass(WordClass.ADVERB);
        word.setName("pool");

        wordService.saveWordToDb(word);

        Assertions.assertEquals(word, wordRepository.findOne(word.getId()));
        Assertions.assertEquals(word.getName(), wordRepository.findByName("pool").get(0).getName());
//        Assertions.assertEquals(word);


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
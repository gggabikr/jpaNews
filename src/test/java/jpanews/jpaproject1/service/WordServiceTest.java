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
    public void findWithString() throws Exception {
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

        KorWord word4 = new KorWord();
        word4.setName("catacomb");
        word4.setKMeaning("지하묘지");
        word4.setWordClass(WordClass.NOUN);

        KorWord word5 = new KorWord();
        word5.setName("meat");
        word5.setKMeaning("고기");
        word5.setWordClass(WordClass.NOUN);

        EngWord word6 = new EngWord();
        word6.setName("great");
        word6.setEMeaning("Something great");
        word6.setWordClass(WordClass.ADJECTIVE);

        EngWord word7 = new EngWord();
        word7.setName("wheat");
        word7.setEMeaning("plant of rice");
        word7.setWordClass(WordClass.NOUN);

        EngWord word8 = new EngWord();
        word8.setName("whaaaaaaeat");
        word8.setEMeaning("plant oaaaaaaaaf rice");
        word8.setWordClass(WordClass.NOUN);

        EngWord word9 = new EngWord();
        word9.setName("aaaaawheataaaaa");
        word9.setEMeaning("plant of rice");
        word9.setWordClass(WordClass.NOUN);

        EngWord word10 = new EngWord();
        word10.setName("wheeeedddat");
        word10.setEMeaning("sssss");
        word10.setWordClass(WordClass.NOUN);

        KorWord word11 = new KorWord();
        word11.setName("hey");
        word11.setKMeaning("어이");
        word11.setWordClass(WordClass.NOUN);

        //when
        wordService.saveWordToDb(word1);
        wordService.saveWordToDb(word2);
        wordService.saveWordToDb(word3);
        wordService.saveWordToDb(word4);
        wordService.saveWordToDb(word5);
        wordService.saveWordToDb(word6);
        wordService.saveWordToDb(word7);
        wordService.saveWordToDb(word8);
        wordService.saveWordToDb(word9);
        wordService.saveWordToDb(word10);
        wordService.saveWordToDb(word11);


        //then
        //just string
        List<Word> list = new ArrayList<>();
        list.add(word2);
        list.add(word4);
        Assertions.assertEquals(list, wordService.findWithString("Cat"));
        list.clear();

        list.add(word7);
        list.add(word8);
        list.add(word9);
        list.add(word10);
        Assertions.assertEquals(list, wordService.findWithString("wH"));
        list.clear();

        //string with asterisks at the end
        list.add(word7);
        list.add(word8);
        list.add(word10);
        Assertions.assertEquals(list, wordService.findWithString("wh*"));
        list.clear();

        list.add(word2);
        list.add(word4);
        Assertions.assertEquals(list, wordService.findWithString("cat*"));
        list.clear();

        //string  with asterisks at the front
        list.add(word3);
        list.add(word5);
        list.add(word6);
        list.add(word7);
        list.add(word8);
        Assertions.assertEquals(list, wordService.findWithString("*eat"));
        list.clear();

        list.add(word2);
        list.add(word3);
        list.add(word5);
        list.add(word6);
        list.add(word7);
        list.add(word8);
        list.add(word10);
        Assertions.assertEquals(list, wordService.findWithString("*at"));
        list.clear();

        //string with asterisks at the front and the end
        list.add(word7);
        list.add(word9);
        list.add(word10);
        Assertions.assertEquals(list, wordService.findWithString("*he*"));
        list.clear();

        //string with asterisks in the middle of it

        //다하고 워드 서비스 if문 다시 손보기.
    }

    @Test
    public void findWithWordClass() throws Exception{
        //given


        //when


        //then


    }
}
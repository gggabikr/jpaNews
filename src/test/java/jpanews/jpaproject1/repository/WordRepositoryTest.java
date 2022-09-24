package jpanews.jpaproject1.repository;

import jpanews.jpaproject1.domain.Words.EngWord;
import jpanews.jpaproject1.domain.Words.KorWord;
import jpanews.jpaproject1.domain.Words.Word;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class WordRepositoryTest {

    @Autowired
    WordRepository wordRepository;

    @Test
    public void save()throws Exception{
        //given
        KorWord word = new KorWord();
        word.setName("Cat");

        //when
        Long wordId = wordRepository.save(word);
        Word foundWord = wordRepository.findOne(wordId);

        //then
        Assertions.assertEquals(word, foundWord);
        Assertions.assertEquals(wordId, foundWord.getId());
        Assertions.assertEquals(word.getName(),foundWord.getName());


    }

    @Test
    public void findAll()throws Exception{
        //given
        KorWord word1 = new KorWord();
        word1.setName("Cat");
        word1.setKMeaning("고양이");

        EngWord word2 = new EngWord();
        word2.setName("Dog");
        word2.setEMeaning("a domesticated carnivorous mammal that typically has a long snout, an acute sense of smell, nonretractable claws, and a barking, howling, or whining voice.");

        KorWord word3 = new KorWord();
        word3.setName("Horse");
        word3.setKMeaning("말");

        System.out.println(word2.getEMeaning());

        //when
        wordRepository.save(word1);
        wordRepository.save(word2);
        wordRepository.save(word3);

        //then
        Assertions.assertEquals(3, wordRepository.findAll().size());
        Assertions.assertEquals("Cat", wordRepository.findAll().get(0).getName());
        Assertions.assertEquals("고양이", wordRepository.findAll().get(0).getMeaning());
        Assertions.assertEquals("a domesticated carnivorous mammal that typically has a long snout, an acute sense of smell, nonretractable claws, and a barking, howling, or whining voice.", wordRepository.findAll().get(1).getMeaning());
        Assertions.assertEquals("Horse", wordRepository.findAll().get(2).getName());
        Assertions.assertEquals("말", wordRepository.findAll().get(2).getMeaning());


    }

    @Test
    public void findByName()throws Exception{

        //given
        EngWord word1 = new EngWord();
        word1.setName("Cat");
        EngWord word2 = new EngWord();
        word2.setName("Catholic");
        EngWord word3 = new EngWord();
        word3.setName("Catalina");
        EngWord word4 = new EngWord();
        word4.setName("vacation");

        //when
        Long savedId = wordRepository.save(word1);
        wordRepository.save(word2);
        wordRepository.save(word3);
        wordRepository.save(word4);

        //then
        Assertions.assertEquals(1, wordRepository.findByName("cat").size());
        Assertions.assertEquals(wordRepository.findOne(savedId), wordRepository.findByName("cat").get(0));
        Assertions.assertEquals(wordRepository.findByName("cat").get(0), wordRepository.findByName("Cat").get(0));
        Assertions.assertEquals(wordRepository.findByName("cAt").get(0), wordRepository.findByName("CaT").get(0));
        Assertions.assertEquals(wordRepository.findByName("CAT").get(0), wordRepository.findByName("cAT").get(0));

    }

    @Test
    public void findByNameStartingWith()throws Exception{

        //given
        EngWord word1 = new EngWord();
        word1.setName("Cat");
        EngWord word2 = new EngWord();
        word2.setName("Catholic");
        EngWord word3 = new EngWord();
        word3.setName("Catalina");
        EngWord word4 = new EngWord();
        word4.setName("vacation");

        //when
        Long savedId = wordRepository.save(word1);
        wordRepository.save(word2);
        wordRepository.save(word3);
        wordRepository.save(word4);

        //then
        Assertions.assertEquals(3, wordRepository.findByNameStartingWith("cat").size());
        Assertions.assertEquals("Catholic", wordRepository.findByNameStartingWith("cat").get(1).getName());
        Assertions.assertEquals(false, wordRepository.findByNameStartingWith("CAT").contains(word4));
        Assertions.assertEquals(true, wordRepository.findByNameStartingWith("caT").contains(word1));
        Assertions.assertEquals(true, wordRepository.findByNameStartingWith("Cat").contains(word2));
        Assertions.assertEquals(true, wordRepository.findByNameStartingWith("cAt").contains(word3));


    }

    @Test
    public void findByNameEndingWith()throws Exception{

        //given
        EngWord word1 = new EngWord();
        word1.setName("tion");
        EngWord word2 = new EngWord();
        word2.setName("onion");
        EngWord word3 = new EngWord();
        word3.setName("tione");
        EngWord word4 = new EngWord();
        word4.setName("vacation");

        //when
        wordRepository.save(word1);
        wordRepository.save(word2);
        wordRepository.save(word3);
        wordRepository.save(word4);

        //then
        Assertions.assertEquals(2, wordRepository.findByNameEndingWith("tion").size());
        Assertions.assertEquals("vacation", wordRepository.findByNameEndingWith("Tion").get(1).getName());
        Assertions.assertEquals(false, wordRepository.findByNameEndingWith("tIon").contains(word2));
        Assertions.assertEquals(false, wordRepository.findByNameEndingWith("tiOn").contains(word3));
        Assertions.assertEquals(true, wordRepository.findByNameEndingWith("tioN").contains(word1));
        Assertions.assertEquals(true, wordRepository.findByNameEndingWith("TION").contains(word4));

    }

    @Test
    public void findByNameContaining()throws Exception{

        //given
        EngWord word1 = new EngWord();
        word1.setName("qwweeeqqtionwqe");
        EngWord word2 = new EngWord();
        word2.setName("onion");
        EngWord word3 = new EngWord();
        word3.setName("ewewewqtinoesaasdda");
        EngWord word4 = new EngWord();
        word4.setName("vacation");

        //when
        wordRepository.save(word1);
        wordRepository.save(word2);
        wordRepository.save(word3);
        wordRepository.save(word4);

        //then
        Assertions.assertEquals(2, wordRepository.findByNameContaining("tio").size());
        Assertions.assertEquals("qwweeeqqtionwqe", wordRepository.findByNameContaining("Tio").get(0).getName());
        Assertions.assertEquals(false, wordRepository.findByNameContaining("tIo").contains(word2));
        Assertions.assertEquals(false, wordRepository.findByNameContaining("tiO").contains(word3));
        Assertions.assertEquals(true, wordRepository.findByNameContaining("TIO").contains(word1));
        Assertions.assertEquals(true, wordRepository.findByNameContaining("TiO").contains(word4));

    }

    @Test(expected = IllegalStateException.class)
    public void TooShortCase() throws Exception{

        //given
        EngWord word1 = new EngWord();
        word1.setName("Cat");
        EngWord word2 = new EngWord();
        word2.setName("Catholic");
        EngWord word3 = new EngWord();
        word3.setName("Catalina");
        EngWord word4 = new EngWord();
        word4.setName("vacation");

        //when
        wordRepository.save(word1);
        wordRepository.save(word2);
        wordRepository.save(word3);
        wordRepository.save(word4);

        List<Word> words = wordRepository.findByName("at");


        //then
        Assertions.fail("error need to be raised.");
    }

    @Test
    public void findAllKorWordByLanguage() throws Exception{
        //given
        KorWord word1 = new KorWord();
        word1.setName("Cat");
        word1.setKMeaning("고양이");

        EngWord word2 = new EngWord();
        word2.setName("Dog");
        word2.setEMeaning("a domesticated carnivorous mammal that typically has a long snout, " +
                "an acute sense of smell, nonretractable claws, and a barking, howling, or whining voice.");

        KorWord word3 = new KorWord();
        word3.setName("Horse");
        word3.setKMeaning("말");

        KorWord word4 = new KorWord();
        word4.setName("Catholic");
        word4.setKMeaning("천주교의");


        EngWord word5 = new EngWord();
        word5.setName("Catalina");
        word5.setEMeaning("Name, female");

        EngWord word6 = new EngWord();
        word6.setName("vacation");
        word6.setEMeaning("an extended period of leisure and recreation, " +
                "especially one spent away from home or in traveling.");

        KorWord word7 = new KorWord();
        word7.setName("vacation");
        word7.setKMeaning("방학");

        //when
        wordRepository.save(word1);
        wordRepository.save(word2);
        wordRepository.save(word3);
        wordRepository.save(word4);
        wordRepository.save(word5);
        wordRepository.save(word6);
        wordRepository.save(word7);

        //then
        Assertions.assertEquals(4, wordRepository.findAllKorWord().size());
        Assertions.assertEquals(3, wordRepository.findAllEngWord().size());
        Assertions.assertTrue(wordRepository.findAllKorWord().contains(word1));
        Assertions.assertTrue(wordRepository.findAllKorWord().contains(word3));
        Assertions.assertTrue(wordRepository.findAllKorWord().contains(word4));
        Assertions.assertTrue(wordRepository.findAllKorWord().contains(word7));
        Assertions.assertFalse(wordRepository.findAllKorWord().contains(word2));
        Assertions.assertFalse(wordRepository.findAllKorWord().contains(word5));
        Assertions.assertFalse(wordRepository.findAllKorWord().contains(word6));

        Assertions.assertFalse(wordRepository.findAllEngWord().contains(word1));
        Assertions.assertFalse(wordRepository.findAllEngWord().contains(word3));
        Assertions.assertFalse(wordRepository.findAllEngWord().contains(word4));
        Assertions.assertFalse(wordRepository.findAllEngWord().contains(word7));
        Assertions.assertTrue(wordRepository.findAllEngWord().contains(word2));
        Assertions.assertTrue(wordRepository.findAllEngWord().contains(word5));
        Assertions.assertTrue(wordRepository.findAllEngWord().contains(word6));

        Assertions.assertEquals("방학", wordRepository.findAllKorWord().get(3).getMeaning());
        Assertions.assertEquals("Name, female", wordRepository.findAllEngWord().get(1).getMeaning());

    }

}
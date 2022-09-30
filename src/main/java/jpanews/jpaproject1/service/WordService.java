package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    @Transactional
    public void saveWordToDb(Word word) throws Exception {
        validateTooManyWords(word);
        wordRepository.save(word);
    }

    private void validateTooManyWords(Word word) throws Exception {
        List<Word> findWords = wordRepository.findByName(word.getName());
        if (findWords.size() >= 10){
            throw new IllegalStateException("There are too many data saved for this word.");
        }
    }

    public Word findById(Long id){
        return wordRepository.findOne(id);
    }

    public List<Word> findAllWords(){
        return wordRepository.findAll();
    }

    public List<Word> findAllKorWords(){
        return wordRepository.findAllKorWord();
    }

    public List<Word> findAllEngWords(){
        return wordRepository.findAllEngWord();
    }

    public List<Word> findWithWordClass(String str) throws Exception {
        return wordRepository.findByWordClass(str);

//            NOUN, VERB, ADJECTIVE, ADVERB,
//            PRONOUN, DETERMINER, PREPOSITION,
//            CONJUNCTION, INTERJECTION
    }

    public List<Word> findWithString(String str) throws Exception{
//        if(!str.matches("[a-zA-Z*]")){
//            throw new IllegalArgumentException("Only alphabetic characters and * are acceptable");
//        }

        int length = str.length();

        boolean ifFirstCharIsStar = String.valueOf(str.charAt(0)).equals("*");
        boolean ifLastCharIsStar = String.valueOf(str.charAt(length - 1)).equals("*");

        String wordWithoutLastChar = str.substring(0, length - 1);
        String wordWithoutFirstChar = str.substring(1);
        String wordWithoutBothEnd = str.substring(1,length - 1);

        List<Word> wordsNameStartingWith = wordRepository.findByNameStartingWith(wordWithoutLastChar);
        List<Word> wordsNameEndingWith = wordRepository.findByNameEndingWith(wordWithoutFirstChar);
        List<Word> exactMatch = wordRepository.findByName(str);
        List<Word> containWords = wordRepository.findByNameContaining(str);

        if(str.matches("[a-zA-Z]+")){   //e.g) 'vacation'
            containWords.removeAll(exactMatch);
            exactMatch.addAll(containWords);
            return exactMatch;
        } else if(ifFirstCharIsStar && ifLastCharIsStar){   //e.g) '*cat*'
            containWords = wordRepository.findByNameContaining(wordWithoutBothEnd);
            wordsNameStartingWith = wordRepository.findByNameStartingWith(wordWithoutBothEnd);
            wordsNameEndingWith = wordRepository.findByNameEndingWith(wordWithoutBothEnd);
            containWords.removeAll(wordsNameStartingWith);
            containWords.removeAll(wordsNameEndingWith);
            return containWords;
        } else if(ifFirstCharIsStar){   //e.g) '*tion'
            return wordsNameEndingWith;
        } else if(ifLastCharIsStar){    //e.g) 'pre*'
            return wordsNameStartingWith;
        } else if(str.matches("[a-zA-Z*]+")){    //e.g) 'pre*tion'
            ArrayList<Word> finalWords = new ArrayList<Word>();
            String[] parts = str.split("\\*");
            wordsNameStartingWith = wordRepository.findByNameStartingWith(parts[0]);
            wordsNameEndingWith = wordRepository.findByNameEndingWith(parts[1]);
            for (Word wordA:wordsNameEndingWith) {
                for (Word wordB:wordsNameStartingWith) {
                    if(wordA.equals(wordB)){
                        finalWords.add(wordA);
                    }
                }
            }
            return finalWords;
        } else{
            return new ArrayList<Word>();
        }
    }
}
package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.WordClass;
import jpanews.jpaproject1.domain.Words.EngWord;
import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
            ArrayList<Word> finalWords = new ArrayList<>();
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
            return new ArrayList<>();
        }
    }

    public void read(String csvFile) {
//        final String delimiter = "[\\(\\)]";

        try {
            File file = new File(csvFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            System.out.println(br.readLine());
            String line = "";
            String[] tempArr = new String[3];
            while((line = br.readLine()) != null) {

                int indexOfBracketOpen = line.indexOf('(');
                int indexOfBracketClose = line.indexOf(')');
//                System.out.println(indexOfBracketOpen + "," + indexOfBracketClose);
                tempArr[0] = line.substring(0, indexOfBracketOpen).trim();
//                System.out.println(tempArr[0]);

                if(indexOfBracketOpen+1 == indexOfBracketClose){
//                    System.out.println("n/a");
                    tempArr[1] = "n/a";
                }else{
//                    System.out.println(line.substring(indexOfBracketOpen+1, indexOfBracketClose-1));
                    tempArr[1] = line.substring(indexOfBracketOpen+1, indexOfBracketClose-1).trim();
                    if(tempArr[1].contains("&")){
                        int i = tempArr[1].indexOf("&");
                        tempArr[1] = tempArr[1].substring(0, i).trim();
                    }
                }
                tempArr[2] = line.substring(indexOfBracketClose+1).trim();
//                System.out.println(Arrays.toString(tempArr));
                EngWord word = new EngWord();
                word.setName(tempArr[0]);


                if(tempArr[1].contains("imp") || tempArr[1].contains("p. p") || tempArr[1].contains("p pr")| tempArr[1].length() == 0 || tempArr[1].equals("n/a")){
                    word.setWordClass(WordClass.NOTABAILABLE);
                } else if (tempArr[1].equals("n") || tempArr[1].equals("n.")){
                    word.setWordClass(WordClass.NOUN);
                } else if (tempArr[1].equals("v") || tempArr[1].contains("v.")){
                    word.setWordClass(WordClass.VERB);
                } else if (tempArr[1].equals("adv") ||tempArr[1].equals("adv.")){
                    word.setWordClass(WordClass.ADVERB);
                } else if(tempArr[1].equals("a") || tempArr[1].equals("a.")){
                    word.setWordClass(WordClass.ADJECTIVE);
                } else if (tempArr[1].equals("pl.") || tempArr[1].equals("pl")){
                    word.setWordClass(WordClass.PLURAL);
                } else if (tempArr[1].equals("prep") || tempArr[1].equals("prep.")){
                    word.setWordClass(WordClass.PREPOSITION);
                } else if (tempArr[1].contains("prefix") ||tempArr[1].equals("pref") ||tempArr[1].equals("pref.")){
                    word.setWordClass(WordClass.PREFIX);
                } else if (tempArr[1].contains("superl")){
                    word.setWordClass(WordClass.SUPERLATIVE);
                } else if (tempArr[1].contains("interj")){
                    word.setWordClass(WordClass.INTERJECTION);
                } else if (tempArr[1].contains("prep")){
                    word.setWordClass(WordClass.PREPOSITION);
                } else if (tempArr[1].contains("pl")){
                    word.setWordClass(WordClass.PLURAL);
                } else if (tempArr[1].contains("adv")){
                    word.setWordClass(WordClass.ADVERB);
                } else if (tempArr[1].contains("a")){
                    word.setWordClass(WordClass.ADJECTIVE);
                } else if (tempArr[1].contains("n") || tempArr[1].contains("n.")){
                    word.setWordClass(WordClass.NOUN);
                } else{
                    word.setWordClass(WordClass.NOTABAILABLE);
                }
                word.setEMeaning(line.substring(indexOfBracketClose+1));
//                wordRepository.save(word);
                saveWordToDb(word);
                System.out.println(word.getName() + ", " +word.getWordClass()
                        + ", " + word.getMeaning());
            }
            br.close();
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }
}
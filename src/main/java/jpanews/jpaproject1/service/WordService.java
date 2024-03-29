package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.WordClass;
import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    @Transactional
    public void saveWordToDb(Word word) throws Exception {
        checkingErrors(word);
        wordRepository.save(word);
    }


    @Transactional
    public HashMap<Long, String> saveWordsToDb(ArrayList<Word> words) throws Exception {
        HashMap<Long, String> failedOnes = new HashMap<>();
        for (Word word : words) {
            try {
                System.out.println(word.getName() + word.getMeaning());
                saveWordToDb(word);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                failedOnes.put(word.getId(), e.getMessage());
            }
        }
        System.out.println("failed ones: "+ failedOnes);
        return failedOnes;
    }

    private void checkingErrors(Word word) throws Exception {
        List<Word> findWords = wordRepository.findByName(word.getName());
        //prevent adding more than 10 different meanings for one word
        if (findWords.size() >= 10){
            throw new IllegalStateException("There are too many data saved for this word.");
        }
        //prevent adding two exact same word data
        for (Word word2: findWords) {
            if(word.getName().equals(word2.getName()) && word.getMeaning().equals(word2.getMeaning())){
                throw new IllegalStateException("There are exact same word in DB already: "
                        + word.getName() + "//" + word.getMeaning());
            }
        }

    }

    @Transactional
    public void updateWord(Word word){
        Word findWord = wordRepository.findOne(word.getId());
        findWord.setName(word.getName());
        findWord.setWordClass(word.getWordClass());
        findWord.setMeaning(word.getMeaning());
        findWord.setLanguage(word.getLanguage());
        //일반적으로 실무에서는 이렇게 set set set으로 변경하지 않는다. 따로 메서드를 만들어서
        //change(price, name, stockQuantity) 뭐 이런식으로 한번에 값을 변경하도록 설계한다.
        //그렇지않으면 유지보수할때 어디서 값이 변경되었는지 찾기가 매우 어렵다.
    }

    public Word findById(Long id){
        return wordRepository.findOne(id);
    }

    public ArrayList<Word> findByIds(Long... ids){
        return wordRepository.findWords(ids);
    }

    public List<Word> findAllWords(){
        return wordRepository.findAll();
    }

//    public List<Word> findAllKorWords(){
//        return wordRepository.findAllKorWord();
//    }
//
//    public List<Word> findAllEngWords(){
//        return wordRepository.findAllEngWord();
//    }

    public List<Word> findWithWordClass(String str) throws Exception {
        return wordRepository.findByWordClass(str);

//            NOUN, VERB, ADJECTIVE, ADVERB,
//            PRONOUN, DETERMINER, PREPOSITION,
//            CONJUNCTION, INTERJECTION
    }

    public List<Word> findWithStringOnlyExactResults(String str) throws Exception{
        return wordRepository.findByName(str);
    }

    public List<Word> findWithString(String str) throws Exception{
//        if(!str.matches("[a-zA-Z*]")){
//            throw new IllegalArgumentException("Only alphabetic characters and * are acceptable");
//        }

        int length = str.length();

        boolean ifFirstCharIsStar = String.valueOf(str.charAt(0)).equals("*");
        boolean ifLastCharIsStar = String.valueOf(str.charAt(length - 1)).equals("*");

        String wordWithoutLastChar;
        String wordWithoutFirstChar;
        String wordWithoutBothEnd;
        wordWithoutLastChar = str.substring(0, length - 1);
        wordWithoutFirstChar = str.substring(1);
        wordWithoutBothEnd = str.substring(1, length - 1);
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
        ArrayList<Word> words = new ArrayList<>();
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
                String subString = line.substring(indexOfBracketClose + 1);
                if(Objects.equals(subString.substring(subString.length() -1), "\"")) {
                    if(subString.endsWith("\"")){
                        subString = subString.substring(0, subString.length() -1);
                    }
                    tempArr[2] = subString.trim();
                }
                tempArr[2] = subString.trim();
//                System.out.println(Arrays.toString(tempArr));
                Word word = new Word();
                word.setName(tempArr[0].replaceAll("\"", ""));
                word.setLanguage("English");


                if(tempArr[1].contains("imp") || tempArr[1].contains("p. p") || tempArr[1].contains("p pr")| tempArr[1].length() == 0 || tempArr[1].equals("n/a")){
                    word.setWordClass(WordClass.NOTAVAILABLE);
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
                    word.setWordClass(WordClass.NOTAVAILABLE);
                }
                word.setMeaning(subString);
//                saveWordToDb(word);
//                System.out.println(word.getName() + ", " +word.getWordClass()
//                        + ", " + word.getMeaning());
                words.add(word);
            }
            saveWordsToDb(words);
            br.close();
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }
}
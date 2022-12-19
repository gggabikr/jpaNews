//package jpanews.jpaproject1;
//
//import jpanews.jpaproject1.domain.WordClass;
////import jpanews.jpaproject1.domain.Words.EngWord;
//
//import jpanews.jpaproject1.domain.Words.Word;
//import jpanews.jpaproject1.repository.WordRepository;
//import lombok.RequiredArgsConstructor;
//import java.io.*;
//
//@RequiredArgsConstructor
//public class uploadWordDataIntoDB {
//
//    private final WordRepository wordRepository;
//
//    public static void read(String csvFile) {
////        final String delimiter = "[\\(\\)]";
//
//        try {
//            File file = new File(csvFile);
//            FileReader fr = new FileReader(file);
//            BufferedReader br = new BufferedReader(fr);
//            System.out.println(br.readLine());
//            String line = "";
//            String[] tempArr = new String[3];
//            while((line = br.readLine()) != null) {
//
//                int indexOfBracketOpen = line.indexOf('(');
//                int indexOfBracketClose = line.indexOf(')');
////                System.out.println(indexOfBracketOpen + "," + indexOfBracketClose);
//                tempArr[0] = line.substring(0, indexOfBracketOpen).trim();
////                System.out.println(tempArr[0]);
//
//                if(indexOfBracketOpen+1 == indexOfBracketClose){
////                    System.out.println("n/a");
//                    tempArr[1] = "n/a";
//                }else{
////                    System.out.println(line.substring(indexOfBracketOpen+1, indexOfBracketClose-1));
//                    tempArr[1] = line.substring(indexOfBracketOpen+1, indexOfBracketClose-1).trim();
//                    if(tempArr[1].contains("&")){
//                        int i = tempArr[1].indexOf("&");
//                        tempArr[1] = tempArr[1].substring(0, i).trim();
//                    }
//                }
//                tempArr[2] = line.substring(indexOfBracketClose+1).trim();
////                System.out.println(Arrays.toString(tempArr));
//                Word word = new Word();
//                word.setName(tempArr[0]);
//
//
//                if(tempArr[1].contains("imp") || tempArr[1].contains("p. p") || tempArr[1].contains("p pr")| tempArr[1].length() == 0 || tempArr[1].equals("n/a")){
//                    word.setWordClass(WordClass.NOTABAILABLE);
//                } else if (tempArr[1].equals("n") || tempArr[1].equals("n.")){
//                    word.setWordClass(WordClass.NOUN);
//                } else if (tempArr[1].equals("v") || tempArr[1].contains("v.")){
//                    word.setWordClass(WordClass.VERB);
//                } else if (tempArr[1].equals("adv") ||tempArr[1].equals("adv.")){
//                    word.setWordClass(WordClass.ADVERB);
//                } else if(tempArr[1].equals("a") || tempArr[1].equals("a.")){
//                    word.setWordClass(WordClass.ADJECTIVE);
//                } else if (tempArr[1].equals("pl.") || tempArr[1].equals("pl")){
//                    word.setWordClass(WordClass.PLURAL);
//                } else if (tempArr[1].equals("prep") || tempArr[1].equals("prep.")){
//                    word.setWordClass(WordClass.PREPOSITION);
//                } else if (tempArr[1].contains("prefix") ||tempArr[1].equals("pref") ||tempArr[1].equals("pref.")){
//                    word.setWordClass(WordClass.PREFIX);
//                } else if (tempArr[1].contains("superl")){
//                    word.setWordClass(WordClass.SUPERLATIVE);
//                } else if (tempArr[1].contains("interj")){
//                    word.setWordClass(WordClass.INTERJECTION);
//                } else if (tempArr[1].contains("prep")){
//                    word.setWordClass(WordClass.PREPOSITION);
//                } else if (tempArr[1].contains("pl")){
//                    word.setWordClass(WordClass.PLURAL);
//                } else if (tempArr[1].contains("adv")){
//                    word.setWordClass(WordClass.ADVERB);
//                } else if (tempArr[1].contains("a")){
//                    word.setWordClass(WordClass.ADJECTIVE);
//                } else if (tempArr[1].contains("n") || tempArr[1].contains("n.")){
//                    word.setWordClass(WordClass.NOUN);
//                } else{
//                    word.setWordClass(WordClass.NOTABAILABLE);
//                }
//                word.setMeaning(line.substring(indexOfBracketClose+1));
////                wordRepository.save(word);
//                System.out.println(word.getName() + ", " +word.getWordClass()
//                        + ", " + word.getMeaning());
//            }
//            br.close();
//        } catch(IOException ioe) {
//            ioe.printStackTrace();
//        }
//    }
//    public static void main(String[] args) {
//        // csv file to read
//        String csvFile = "src/main/java/jpanews/jpaproject1/VocabularyData/A.csv";
////        csvFile = "/Users/jasonlee/Desktop/uploadedFile/A(1).xlsx";
//        uploadWordDataIntoDB.read(csvFile);
//    }
//}
//
//
